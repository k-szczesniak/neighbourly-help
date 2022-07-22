package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Advert;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Contract;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.LoyaltyPoint;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.repository.LoyaltyPointRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.ClientData;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Role;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.enums.AccessLevel;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.LoyaltyPointException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.NotFoundException;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY, rollbackFor = AppBaseException.class)
public class LoyaltyPointService {

    private final LoyaltyPointRepository loyaltyPointRepository;

    @Secured({"ROLE_CLIENT"})
    public LoyaltyPoint get(Long loyaltyPointId) throws AppBaseException {
        return loyaltyPointRepository.findById(loyaltyPointId).orElseThrow(NotFoundException::loyaltyPointNotFound);
    }

    @Secured({"ROLE_CLIENT"})
    public void blockLoyaltyPoints(Account account, BigInteger prize) throws AppBaseException {
        ClientData publisher = extractClientData(account);

        LoyaltyPoint publisherLoyaltyPoint = get(publisher.getLoyaltyPoint().getId());

        BigInteger totalPoints = publisherLoyaltyPoint.getTotalPoints();
        BigInteger blockedPoints = publisherLoyaltyPoint.getBlockedPoints();
        conditionVerifier(totalPoints.compareTo(prize) < 0, LoyaltyPointException.loyaltyPointsAccountBalanceExceeded());
        // TODO: 22.07.2022 more condition cases

        BigInteger newTotalPointsValue = totalPoints.subtract(prize);
        BigInteger newBlockedPointsValue = blockedPoints.add(prize);

        publisherLoyaltyPoint.setTotalPoints(newTotalPointsValue);
        publisherLoyaltyPoint.setBlockedPoints(newBlockedPointsValue);
        publisherLoyaltyPoint.setModifiedBy(account);

        loyaltyPointRepository.saveAndFlush(publisherLoyaltyPoint);

    }

    @Secured({"ROLE_CLIENT"})
    public void recalculatePointsBalance(Account account, Advert advert, BigInteger oldPrize) throws AppBaseException {
        ClientData publisher = extractClientData(account);

        LoyaltyPoint publisherLoyaltyPoint = get(publisher.getLoyaltyPoint().getId());

        BigInteger totalPoints = publisherLoyaltyPoint.getTotalPoints();
        BigInteger blockedPoints = publisherLoyaltyPoint.getBlockedPoints();
        BigInteger difference = advert.getPrize().subtract(oldPrize);

        BigInteger newTotalPointsValue;
        BigInteger newBlockedPointsValue;

        if(difference.compareTo(BigInteger.ZERO) > 0) {
            conditionVerifier(totalPoints.compareTo(difference) < 0, LoyaltyPointException.loyaltyPointsAccountBalanceExceeded());
            // TODO: 22.07.2022 more condition cases
            newTotalPointsValue = totalPoints.subtract(difference);
            newBlockedPointsValue = blockedPoints.add(difference);
        } else {
            difference = difference.negate();
            newTotalPointsValue = totalPoints.add(difference);
            newBlockedPointsValue = blockedPoints.subtract(difference);
        }

        publisherLoyaltyPoint.setTotalPoints(newTotalPointsValue);
        publisherLoyaltyPoint.setBlockedPoints(newBlockedPointsValue);
        publisherLoyaltyPoint.setModifiedBy(account);

        loyaltyPointRepository.saveAndFlush(publisherLoyaltyPoint);
    }

    @Secured({"ROLE_CLIENT"})
    public void executeDonate(Contract contract) throws AppBaseException {

        Advert advert = contract.getAdvert();

        ClientData publisher = extractClientData(advert.getPublisher());

        LoyaltyPoint publisherLoyaltyPoint = get(publisher.getLoyaltyPoint().getId());
        BigInteger publisherBlockedPoints = publisherLoyaltyPoint.getBlockedPoints();

        BigInteger prize = advert.getPrize();

        BigInteger newBlockedPointsValue = publisherBlockedPoints.subtract(prize);
        publisherLoyaltyPoint.setBlockedPoints(newBlockedPointsValue);

        loyaltyPointRepository.saveAndFlush(publisherLoyaltyPoint);

        // add points to advert executor
        ClientData contractExecutor = extractClientData(contract.getExecutor());

        LoyaltyPoint executorLoyaltyPoint = get(contractExecutor.getLoyaltyPoint().getId());
        BigInteger contractExecutorTotalPoints = executorLoyaltyPoint.getTotalPoints();

        BigInteger newContractExecutorTotalPointsValue = contractExecutorTotalPoints.add(prize);
        executorLoyaltyPoint.setTotalPoints(newContractExecutorTotalPointsValue);

        loyaltyPointRepository.saveAndFlush(executorLoyaltyPoint);

    }

    private ClientData extractClientData(Account account) throws NotFoundException {
        return (ClientData) account.getRoleList().stream()
                .filter(Role::isEnabled)
                .filter(role -> role.getAccessLevel().equals(AccessLevel.CLIENT))
                .findAny()
                .orElseThrow(NotFoundException::enabledClientRoleNotFound);
    }

    private void conditionVerifier(boolean condition, LoyaltyPointException exception) throws LoyaltyPointException {
        if (condition) {
            throw exception;
        }
    }

}