package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Contract;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.ApproveFinishedRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewContractRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.ContractDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.DetailContractDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.mapper.ContractMapper;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.service.ContractService;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.ContractException;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEndpoint;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = AppBaseException.class)
public class ContractHelperImpl extends AbstractEndpoint implements ContractHelper {

    private final ContractService contractService;

    @Override
    @Secured({"ROLE_CLIENT"})
    public DetailContractDto get(Long contractId) throws AppBaseException {
        Contract contract = contractService.get(contractId);

        if (getEmail().equals(contract.getExecutor().getEmail())) {
            return Mappers.getMapper(ContractMapper.class).toDetailContractDto(contract);
        } else {
            throw ContractException.accessDenied();
        }
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public void createContract(NewContractRequestDto newContract) throws AppBaseException {
        contractService.createContract(newContract);
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public void cancelContract(Long contractId, String ifMatch) throws AppBaseException {
        Contract contract = contractService.get(contractId);

        if (getEmail().equals(contract.getExecutor().getEmail())) {
            DetailContractDto contractIntegrity = Mappers.getMapper(ContractMapper.class).toDetailContractDto(contract);
            verifyIntegrity(contractIntegrity, ifMatch);
            contractService.cancelContract(contract);
        } else {
            throw ContractException.accessDenied();
        }
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public void startContract(Long contractId, String ifMatch) throws AppBaseException {
        Contract contract = contractService.get(contractId);
        verifyPrivilegesAndIntegrity(() -> contractService.startContract(contract), contract, ifMatch);
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public void endContract(Long contractId, String ifMatch) throws AppBaseException {
        Contract contract = contractService.get(contractId);
        verifyPrivilegesAndIntegrity(() -> contractService.endContract(contract), contract, ifMatch);
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public void approveFinishedContract(ApproveFinishedRequestDto approveFinishedRequestDto, String ifMatch) throws AppBaseException {
        Contract contract = contractService.get(approveFinishedRequestDto.getContractId());
        verifyPrivilegesAndIntegrity(() -> contractService.approveFinishedContract(contract, approveFinishedRequestDto),
                contract, ifMatch);
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public List<ContractDto> getMyActiveContract() throws AppBaseException {
        List<Contract> activeContract = contractService.getMyActiveContract();
        return toListOfContractResponseDto(activeContract);
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public List<ContractDto> getDelegateActiveContracts() throws AppBaseException {
        List<Contract> activeContract = contractService.getDelegateActiveContracts();
        return toListOfContractResponseDto(activeContract);
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public List<ContractDto> getMyFinishedContract() throws AppBaseException {
        List<Contract> finishedContract = contractService.getMyFinishedContract();
        return toListOfContractResponseDto(finishedContract);
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public List<ContractDto> getDelegateFinishedContracts() throws AppBaseException {
        List<Contract> finishedContract = contractService.getDelegateFinishedContracts();
        return toListOfContractResponseDto(finishedContract);
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public List<ContractDto> getUnratedFinishedContracts() throws AppBaseException {
        List<Contract> unratedFinishedContracts = contractService.getUnratedFinishedContracts();
        return toListOfContractResponseDto(unratedFinishedContracts);
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public List<ContractDto> getMyUnpaidFinishedContracts() throws AppBaseException {
        List<Contract> unpaidFinishedContracts = contractService.getMyUnpaidFinishedContracts();
        return toListOfContractResponseDto(unpaidFinishedContracts);
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public List<ContractDto> getDelegateUnpaidFinishedContracts() throws AppBaseException {
        List<Contract> unpaidFinishedContracts = contractService.getDelegateUnpaidFinishedContracts();
        return toListOfContractResponseDto(unpaidFinishedContracts);
    }

    private void verifyPrivilegesAndIntegrity(VoidMethodExecutor executor, Contract contract, String ifMatch) throws AppBaseException {
        if (getEmail().equals(contract.getExecutor().getEmail()) ||
                getEmail().equals(contract.getAdvert().getPublisher().getEmail())) {
            DetailContractDto contractIntegrity = Mappers.getMapper(ContractMapper.class).toDetailContractDto(contract);
            verifyIntegrity(contractIntegrity, ifMatch);
            executor.run();
        } else {
            throw ContractException.accessDenied();
        }
    }

    private List<ContractDto> toListOfContractResponseDto(List<Contract> contractList) {
        ContractMapper contractMapper = Mappers.getMapper(ContractMapper.class);
        return contractList.stream().map(contractMapper::toContractDto).collect(Collectors.toList());
    }

    @FunctionalInterface
    public interface VoidMethodExecutor {
        void run() throws AppBaseException;
    }
}