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
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.UpdateRatingDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.repository.RatingRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.service.AccountService;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.NotFoundException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.RatingException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY, rollbackFor = AppBaseException.class)
public class RatingService {

    private final RatingRepository ratingRepository;
    private final ContractService contractService;
    private final AccountService accountService;

    @Secured({"ROLE_MODERATOR", "ROLE_CLIENT"})
    public Rating get(Long ratingId) throws AppBaseException {
        return ratingRepository.findById(ratingId).orElseThrow(NotFoundException::ratingNotFound);
    }

    @Secured({"ROLE_MODERATOR", "ROLE_CLIENT"})
    public List<Rating> getAllAccountRatings(Long accountId) {
        return ratingRepository.findAll().stream()
                .filter(rating -> rating.getContract().getExecutor().getId().equals(accountId) ||
                        rating.getContract().getAdvert().getPublisher().getId().equals(accountId))
                .filter(rating -> !rating.getCreatedBy().getId().equals(accountId))
                .collect(Collectors.toList());
    }

    @Secured({"ROLE_CLIENT"})
    public void addRating(NewRatingDto newRatingDto) throws AppBaseException {
        Contract ratedContract = contractService.get(newRatingDto.getContractId());

        conditionVerifier(ratedContract == null, RatingException.contractNotExists());
        conditionVerifier(!ratedContract.getStatus().equals(ContractStatus.FINISHED), RatingException.contractNotFinished());
        conditionVerifier(ratedContract.getRating() != null, RatingException.ratingAlreadyExists());

        Account executorAccount = accountService.getExecutorAccount();
        boolean isAdvertPublisher = ratedContract.getAdvert().getPublisher().equals(executorAccount);
        conditionVerifier(!isAdvertPublisher, RatingException.accessDenied());

        Rating rating = Rating.builder()
                .rate(newRatingDto.getRate())
                .contract(ratedContract)
                .hidden(false)
                .comment(newRatingDto.getComment())
                .build();
        rating.setCreatedBy(executorAccount);

        ratingRepository.saveAndFlush(rating);

        Account accountToRate = ratedContract.getExecutor();

        BigDecimal averageRating = calculateAverageRating(accountToRate.getId());
        accountService.updateAccountRating(accountToRate.getId(), averageRating);
    }

    @Secured({"ROLE_CLIENT"})
    public void updateRating(UpdateRatingDto updateRatingDto) throws AppBaseException {
        Rating rating = get(updateRatingDto.getId());

        Account executorAccount = accountService.getExecutorAccount();
        rating.setRate(updateRatingDto.getRate());
        rating.setComment(updateRatingDto.getComment());
        rating.setModifiedBy(executorAccount);
        ratingRepository.saveAndFlush(rating);

        Account ratingAuthor = rating.getCreatedBy();
        boolean isAdvertPublisher = ratingAuthor.equals(executorAccount);
        Account accountToRate = isAdvertPublisher ? rating.getContract().getExecutor() : rating.getContract().getAdvert().getPublisher();

        BigDecimal averageRating = calculateAverageRating(accountToRate.getId());
        accountService.updateAccountRating(accountToRate.getId(), averageRating);
    }

    @Secured({"ROLE_MODERATOR"})
    public void changeVisibility(Rating rating) throws AppBaseException {
        rating.setHidden(!rating.isHidden());
        rating.setModifiedBy(accountService.getExecutorAccount());

        ratingRepository.saveAndFlush(rating);
    }

    @Secured({"ROLE_CLIENT"})
    public void deleteRating(Rating rating) throws AppBaseException {
        Account executorAccount = accountService.getExecutorAccount();

        Account ratingAuthor = rating.getCreatedBy();
        boolean isAdvertPublisher = ratingAuthor.equals(executorAccount);
        Account accountToRate = isAdvertPublisher ? rating.getContract().getExecutor() : rating.getContract().getAdvert().getPublisher();

        ratingRepository.delete(rating);

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