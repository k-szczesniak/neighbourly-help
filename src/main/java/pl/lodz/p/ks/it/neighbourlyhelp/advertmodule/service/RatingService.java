package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Contract;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Rating;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.enums.ContractStatus;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewRatingDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.repository.RatingRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.service.AccountService;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.RatingException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY, rollbackFor = AppBaseException.class)
public class RatingService {

    private final RatingRepository ratingRepository;
    private final ContractService contractService;
    private final AccountService accountService;

    @Secured({"ROLE_ADMIN"})
    public void addRating(NewRatingDto newRatingDto) throws AppBaseException {
        Contract ratedContract = contractService.get(newRatingDto.getContractId());

        conditionVerifier(ratedContract == null, RatingException.contractNotExists());
        conditionVerifier(!ratedContract.getStatus().equals(ContractStatus.FINISHED), RatingException.contractNotFinished());
        conditionVerifier(ratedContract.getRating() != null, RatingException.ratingAlreadyExists());

        Account executorAccount = accountService.getExecutorAccount();
        boolean isAdvertPublisher = ratedContract.getAdvert().getPublisher().equals(executorAccount);
        boolean isContractExecutor = ratedContract.getExecutor().equals(executorAccount);
        conditionVerifier(!isAdvertPublisher && !isContractExecutor, RatingException.accessDenied());

        Rating rating = Rating.builder()
                .rate(newRatingDto.getRate())
                .contract(ratedContract)
                .hidden(false)
                .comment(newRatingDto.getComment())
                .build();
        rating.setCreatedBy(executorAccount);

        ratingRepository.saveAndFlush(rating);

        Account accountToRate = isAdvertPublisher ? ratedContract.getExecutor() : ratedContract.getAdvert().getPublisher();

        BigDecimal averageRating = calculateAverageRating(accountToRate.getId());
        accountService.updateAccountRating(accountToRate.getId(), averageRating);
    }

    private BigDecimal calculateAverageRating(Long accountId) throws AppBaseException {
        List<Rating> ratings = ratingRepository.findAllByAccountId(accountId);
        if (ratings.isEmpty()) {
            return null;
        }
        return BigDecimal.valueOf(ratings.stream()
                .mapToDouble(Rating::getRate)
                .average()
                .getAsDouble()).setScale(1, RoundingMode.HALF_UP);
    }

    private void conditionVerifier(boolean condition, RatingException exception) throws RatingException {
        if (condition) {
            throw exception;
        }
    }
}