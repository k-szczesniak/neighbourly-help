package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Advert;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Contract;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.LoyaltyPoint;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.enums.ContractStatus;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.enums.LoyaltyPointStatus;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.ApproveFinishedRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewContractRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.repository.ContractRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.service.AccountService;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.ContractException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.NotFoundException;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.email.EmailService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY, rollbackFor = AppBaseException.class)
public class ContractService {

    private final ContractRepository contractRepository;

    private final AdvertService advertService;
    private final AccountService accountService;
    private final EmailService emailService;


    @Value("${contract.maxTakeUpAttempts}")
    private Integer maxAttempts;

    @Secured({"ROLE_CLIENT"})
    public Contract get(Long contractId) throws AppBaseException {
        return contractRepository.findById(contractId).orElseThrow(NotFoundException::contractNotFound);
    }

    @Secured({"ROLE_CLIENT"})
    public void createContract(NewContractRequestDto newContract) throws AppBaseException {
        Long advertId = newContract.getAdvertId();
        Advert advert = advertService.get(advertId);
        Account executorAccount = accountService.getExecutorAccount();

        List<Contract> allAssociatedContractsWithAdvert = contractRepository.findAll().stream()
                .filter(contract -> contract.getAdvert().getId().equals(advertId))
                .collect(Collectors.toList());

        conditionVerifier(!advert.isApproved(), ContractException.advertIsDisapproved());
        conditionVerifier(allAssociatedContractsWithAdvert.stream()
                        .anyMatch(contract -> !contract.getStatus().equals(ContractStatus.CANCELLED)),
                ContractException.advertAlreadyTaken());
        conditionVerifier(allAssociatedContractsWithAdvert.stream()
                        .filter(contract -> contract.getExecutor().equals(executorAccount))
                        .filter(contract -> contract.getStatus().equals(ContractStatus.CANCELLED))
                        .count() >= maxAttempts,
                ContractException.maxTakeUpAttemptsLimitOverdrawn());

        // TODO: 16.05.2022 more validation cases

        Contract contract = Contract.builder()
                .advert(advert)
                .executor(executorAccount)
                .status(ContractStatus.NEW)
                .build();

        contract.setCreatedBy(executorAccount);

        contractRepository.saveAndFlush(contract);
        emailService.sendCreateContractEmail(executorAccount, contract.getId(), advert.getTitle());
    }

    @Secured({"ROLE_CLIENT"})
    public void cancelContract(Contract contract) throws AppBaseException {
        if (contract.getStatus().equals(ContractStatus.NEW)) {
            Account modifier = contract.getExecutor();
            contract.setStatus(ContractStatus.CANCELLED);
            contract.setModifiedBy(modifier);
            contractRepository.saveAndFlush(contract);
            emailService.sendCancelContractEmail(modifier, contract.getId(), contract.getAdvert().getTitle());
        } else if (contract.getStatus().equals(ContractStatus.IN_PROGRESS)) {
            throw ContractException.inProgressContractCancellation();
        } else if (contract.getStatus().equals(ContractStatus.TO_APPROVE)) {
            throw ContractException.toApproveContractCancellation();
        } else if (contract.getStatus().equals(ContractStatus.FINISHED)) {
            throw ContractException.finishedContractCancellation();
        } else {
            throw ContractException.contractAlreadyCancelled();
        }
    }

    @Secured({"ROLE_CLIENT"})
    public void startContract(Contract contract) throws AppBaseException {
        if (contract.getStatus().equals(ContractStatus.NEW)) {
            Account modifier = accountService.getExecutorAccount();
            contract.setStatus(ContractStatus.IN_PROGRESS);
            contract.setModifiedBy(modifier);
            contract.setStartDate(new Date());
            contractRepository.saveAndFlush(contract);
        } else if (contract.getStatus().equals(ContractStatus.IN_PROGRESS)) {
            throw ContractException.inProgressContractCancellation();
        } else if (contract.getStatus().equals(ContractStatus.TO_APPROVE)) {
            throw ContractException.toApproveContractCancellation();
        } else if (contract.getStatus().equals(ContractStatus.FINISHED)) {
            throw ContractException.finishedContractCancellation();
        } else {
            throw ContractException.contractAlreadyCancelled();
        }
    }

    @Secured({"ROLE_CLIENT"})
    public void endContract(Contract contract) throws AppBaseException {
        if (contract.getStatus().equals(ContractStatus.IN_PROGRESS)) {
            Account modifier = accountService.getExecutorAccount();
            contract.setStatus(ContractStatus.TO_APPROVE);
            contract.setModifiedBy(modifier);
            contract.setFinishDate(new Date());
            contractRepository.saveAndFlush(contract);
            emailService.sendWaitingToApproveContractEmail(contract.getExecutor(), contract.getAdvert().getPublisher(),
                    contract.getId(), contract.getAdvert().getTitle());
        } else if (contract.getStatus().equals(ContractStatus.NEW)) {
            throw ContractException.contractNotStartedYet();
        } else if (contract.getStatus().equals(ContractStatus.TO_APPROVE)) {
            throw ContractException.toApproveContractCancellation();
        } else if (contract.getStatus().equals(ContractStatus.FINISHED)) {
            throw ContractException.finishedContractCancellation();
        } else {
            throw ContractException.contractAlreadyCancelled();
        }
    }

    @Secured({"ROLE_CLIENT"})
    public void approveFinishedContract(Contract contract, ApproveFinishedRequestDto requestDto) throws AppBaseException {
        if (contract.getStatus().equals(ContractStatus.TO_APPROVE)) {
            Account modifier = accountService.getExecutorAccount();
            contract.setStatus(ContractStatus.FINISHED);
            contract.setModifiedBy(modifier);

            LoyaltyPoint loyaltyPoint = LoyaltyPoint.builder()
                    .contract(contract)
                    .status(LoyaltyPointStatus.valueOf(requestDto.getLoyaltyPointStatus()))
                    .comment(requestDto.getComment())
                    .build();
            loyaltyPoint.setCreatedBy(modifier);

            contract.setLoyaltyPoint(loyaltyPoint);
            contractRepository.saveAndFlush(contract);

        } else if (contract.getStatus().equals(ContractStatus.NEW)) {
            throw ContractException.contractNotStartedYet();
        } else if (contract.getStatus().equals(ContractStatus.IN_PROGRESS)) {
            throw ContractException.inProgressContractCancellation();
        } else if (contract.getStatus().equals(ContractStatus.FINISHED)) {
            throw ContractException.finishedContractCancellation();
        } else {
            throw ContractException.contractAlreadyCancelled();
        }
    }

    private void conditionVerifier(boolean condition, ContractException exception) throws ContractException {
        if (condition) {
            throw exception;
        }
    }
}