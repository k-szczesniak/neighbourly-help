package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Advert;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.enums.ContractStatus;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.repository.AdvertRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Role;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.service.AccountService;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AdvertException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.NotFoundException;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY, rollbackFor = AppBaseException.class)
public class AdvertService {

    private final AdvertRepository advertRepository;

    private final CityService cityService;

    private final AccountService accountService;

    private final LoyaltyPointService loyaltyPointService;

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public Advert get(Long advertId) throws AppBaseException {
        return advertRepository.findById(advertId).orElseThrow(NotFoundException::advertNotFound);
    }

    @Secured({"ROLE_ADMIN"})
    public List<Advert> getAllAdverts() {
        return advertRepository.findAll();
    }

    @Secured({"ROLE_MODERATOR", "ROLE_CLIENT"})
    public List<Advert> getAllApprovedAdvertsToTake() {
        Predicate<Advert> advertPredicate = getAdvertPredicate();
        return advertRepository.findAll().stream()
                .filter(Advert::isApproved)
                .filter(advertPredicate)
                .collect(Collectors.toList());
    }

    @Secured({"ROLE_MODERATOR"})
    public List<Advert> getAllWaitingToApproveAdverts(String executorEmail) {
        return advertRepository.findAll().stream()
                .filter(advert -> !advert.isApproved())
                .filter(advert -> verifyExecutorPrivileges(advert, executorEmail))
                .collect(Collectors.toList());
    }

//    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
//    public List<Advert> getAllActiveAdverts() {
//        return getAllAdverts().stream()
//                .filter(advert -> advert.getContract() != null)
//                .collect(Collectors.toList());
//    }

    @Secured({"ROLE_CLIENT"})
    public List<Advert> getAllOwnAdverts() throws AppBaseException {
        Account executorAccount = accountService.getExecutorAccount();
        return advertRepository.findAll().stream()
                .filter(advert -> advert.getPublisher() == executorAccount)
                .collect(Collectors.toList());
    }

    @Secured({"ROLE_CLIENT"})
    public List<Advert> getAllClientAdverts(Long userId) throws AppBaseException {
        Account account = accountService.getAccountById(userId);
        return advertRepository.findAll().stream()
                .filter(Advert::isApproved)
                .filter(advert -> advert.getPublisher() == account)
                .filter(getAdvertPredicate())
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

        loyaltyPointService.blockLoyaltyPoints(publisher, advert.getPrize());
    }

    @Secured({"ROLE_CLIENT"})
    public void updateAdvert(Advert advert, @NotNull Long cityId, String executorEmail, BigInteger oldPrize) throws AppBaseException {
        verifyIfAdvertIsRelatedWithContractInProgress(advert);
        conditionVerifier(!advert.getPublisher().getEmail().equals(executorEmail), AdvertException.accessDenied());

        Account editor = accountService.getExecutorAccount();
        advert.setModifiedBy(editor);
        advert.setCity(cityService.get(cityId));
        advert.setApproved(false);

        loyaltyPointService.recalculatePointsBalance(editor, advert, oldPrize);

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
        verifyCanBeDisapproved(advert, executorEmail);

        advert.setApproved(false);
        advert.setModifiedBy(accountService.getExecutorAccount());
    }

    @Secured({"ROLE_CLIENT"})
    public void deleteAdvert(Advert advert, String executorEmail) throws AppBaseException {
        verifyCanBeDeleted(advert, executorEmail);

        loyaltyPointService.unblockPoints(advert.getPublisher(), advert.getPrize());

        advertRepository.delete(advert);
    }

    @Secured({"ROLE_CLIENT"})
    public boolean isActiveContract(Advert advert) {
        Predicate<Advert> advertPredicate = getAdvertPredicate();
        return advertPredicate.test(advert);
    }

    private Predicate<Advert> getAdvertPredicate() {
        return advert -> advert.getContractList().stream()
                .noneMatch(contract -> contract.getAdvert().getId().equals(advert.getId())
                        && !contract.getStatus().equals(ContractStatus.CANCELLED));
    }

    private void verifyIfAdvertIsRelatedWithContractInProgress(Advert advertToProcess) throws AdvertException {
        conditionVerifier(advertToProcess.getContractList().stream()
                        .anyMatch(contract -> contract.getStatus().equals(ContractStatus.IN_PROGRESS)),
                AdvertException.advertIsInProgress());
    }

    private void verifyCanBeDisapproved(Advert advertToProcess, String executorEmail) throws AppBaseException {
        conditionVerifier(!verifyExecutorPrivileges(advertToProcess, executorEmail), AdvertException.accessDenied());

        verifyIfAdvertIsRelatedWithContractInProgress(advertToProcess);

        conditionVerifier(!advertToProcess.isApproved(), AdvertException.advertIsDisapproved());
    }

    private void verifyCanBeDeleted(Advert advertToProcess, String executorEmail) throws AppBaseException {
        conditionVerifier(!verifyUserIsAdvertPublisher(advertToProcess, executorEmail), AdvertException.accessDenied());

        verifyIfAdvertIsRelatedWithContractInProgress(advertToProcess);

        conditionVerifier(!advertToProcess.isApproved(), AdvertException.advertIsDisapproved());
    }

    private boolean verifyExecutorPrivileges(Advert advertToProcess, String executorEmail) {
        return advertToProcess.getCity().getModeratorDataList().stream()
                .filter(Role::isEnabled)
                .anyMatch(moderator -> moderator.getAccount().getEmail().equals(executorEmail));
    }

    private boolean verifyUserIsAdvertPublisher(Advert advertToProcess, String executorEmail) {
        return advertToProcess.getPublisher().getEmail().equals(executorEmail);
    }

    private void conditionVerifier(boolean condition, AdvertException exception) throws AdvertException {
        if (condition) {
            throw exception;
        }
    }
}