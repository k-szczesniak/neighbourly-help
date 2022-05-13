package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Advert;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Contract;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.enums.ContractStatus;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.repository.AdvertRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Role;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.service.AccountService;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AdvertException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.NotFoundException;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY, rollbackFor = AppBaseException.class)
public class AdvertService {

    private final AdvertRepository advertRepository;

    private final CityService cityService;

    private final AccountService accountService;

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public Advert get(Long advertId) throws AppBaseException {
        return advertRepository.findById(advertId).orElseThrow(NotFoundException::advertNotFound);
    }

    @Secured({"ROLE_ADMIN"})
    public List<Advert> getAllAdverts() {
        return advertRepository.findAll();
    }

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public List<Advert> getAllApprovedAdverts() {
        return advertRepository.findAll().stream()
                .filter(Advert::isApproved)
                .collect(Collectors.toList());
    }

    @Secured({"ROLE_MODERATOR"})
    public List<Advert> getAllWaitingToApproveAdverts(String executorEmail) {
        return advertRepository.findAll().stream()
                .filter(advert -> !advert.isApproved())
                .filter(advert -> verifyExecutorPrivileges(advert, executorEmail))
                .collect(Collectors.toList());
    }

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public List<Advert> getAllActiveAdverts() {
        return getAllAdverts().stream()
                .filter(advert -> advert.getContract() != null)
                .collect(Collectors.toList());
    }

    @Secured({"ROLE_CLIENT"})
    public void addAdvert(Advert advert, @NotNull Long cityId) throws AppBaseException {
        advert.setCity(cityService.get(cityId));
        advert.setApproved(false);
        Account publisher = accountService.getExecutorAccount();
        advert.setPublisher(publisher);
        advert.setCreatedBy(publisher);
        advert.setPublicationDate(new Date());
        advertRepository.saveAndFlush(advert);
    }

    @Secured({"ROLE_CLIENT"})
    public void updateAdvert(Advert advert, @NotNull Long cityId, String executorEmail) throws AppBaseException {
        conditionVerifier(advert.getContract() != null, AdvertException.advertIsInProgress());
        conditionVerifier(!advert.getPublisher().getEmail().equals(executorEmail), AdvertException.accessDenied());

        advert.setModifiedBy(accountService.getExecutorAccount());
        advert.setCity(cityService.get(cityId));
        advert.setApproved(false);

        advertRepository.saveAndFlush(advert);
    }

    @Secured({"ROLE_MODERATOR"})
    public void approveAdvert(Advert advert, String executorEmail) throws AppBaseException {
        verifyExecutorPrivileges(advert, executorEmail);
        advert.setApproved(true);
        advert.setModifiedBy(accountService.getExecutorAccount());
    }

    @Secured({"ROLE_MODERATOR"})
    public void disapproveAdvert(Advert advert, String executorEmail) throws AppBaseException {
        verifyCanBeDisapprovedOrDelete(advert, executorEmail);

        advert.setApproved(false);
        advert.setModifiedBy(accountService.getExecutorAccount());
    }

    @Secured({"ROLE_CLIENT"})
    public void deleteAdvert(Advert advert, String executorEmail) throws AppBaseException {
        verifyCanBeDisapprovedOrDelete(advert, executorEmail);

        advertRepository.delete(advert);
    }

    private void verifyCanBeDisapprovedOrDelete(Advert advertToProcess, String executorEmail) throws AppBaseException {
        conditionVerifier(!verifyExecutorPrivileges(advertToProcess, executorEmail), AdvertException.accessDenied());

        Contract contract = advertToProcess.getContract();
        if (contract != null) {
            conditionVerifier(contract.getStatus().equals(ContractStatus.IN_PROGRESS), AdvertException.advertIsInProgress());
        }

        conditionVerifier(!advertToProcess.isApproved(), AdvertException.advertIsDisapproved());
    }

    private boolean verifyExecutorPrivileges(Advert advertToProcess, String executorEmail) {
        return advertToProcess.getCity().getModeratorDataList().stream()
                .filter(Role::isEnabled)
                .anyMatch(moderator -> moderator.getAccount().getEmail().equals(executorEmail));
    }

    private void conditionVerifier(boolean condition, AdvertException exception) throws AdvertException {
        if (condition) {
            throw exception;
        }
    }
}