package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Rating;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewRatingDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.UpdateRatingDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.RatingDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.mapper.RatingMapper;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.service.RatingService;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.RatingException;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEndpoint;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = AppBaseException.class)
public class RatingHelperImpl extends AbstractEndpoint implements RatingHelper {

    private final RatingService ratingService;

    @Override
    @Secured({"ROLE_MODERATOR", "ROLE_CLIENT"})
    public RatingDto get(Long ratingId) throws AppBaseException {
        return Mappers.getMapper(RatingMapper.class).toRatingDto(ratingService.get(ratingId));
    }

    @Override
    @Secured({"ROLE_MODERATOR", "ROLE_CLIENT"})
    public List<RatingDto> getAllAccountRatings(Long accountId) {
        List<Rating> ratingList = ratingService.getAllAccountRatings(accountId);

        RatingMapper ratingMapper = Mappers.getMapper(RatingMapper.class);
        return ratingList.stream().map(ratingMapper::toRatingDto).collect(Collectors.toList());
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public void addRating(NewRatingDto newRatingDto) throws AppBaseException {
        ratingService.addRating(newRatingDto);
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public void updateRating(UpdateRatingDto updateRatingDto, String ifMatch) throws AppBaseException {
        Rating rating = ratingService.get(updateRatingDto.getId());

        if (getEmail().equals(rating.getCreatedBy().getEmail())) {
            RatingDto ratingIntegrity = Mappers.getMapper(RatingMapper.class).toRatingDto(rating);
            verifyIntegrity(ratingIntegrity, ifMatch);
            ratingService.updateRating(updateRatingDto);
        } else {
            throw RatingException.accessDenied();
        }
    }

    @Override
    @Secured({"ROLE_MODERATOR"})
    public void changeVisibility(Long ratingId, String ifMatch) throws AppBaseException {
        Rating rating = ratingService.get(ratingId);
        RatingDto ratingIntegrity = Mappers.getMapper(RatingMapper.class).toRatingDto(rating);
        verifyIntegrity(ratingIntegrity, ifMatch);

        ratingService.changeVisibility(rating);
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public void deleteRating(Long ratingId, String ifMatch) throws AppBaseException {
        Rating rating = ratingService.get(ratingId);

        if (getEmail().equals(rating.getCreatedBy().getEmail())) {
            RatingDto ratingIntegrity = Mappers.getMapper(RatingMapper.class).toRatingDto(rating);
            verifyIntegrity(ratingIntegrity, ifMatch);
            ratingService.deleteRating(rating);
        } else {
            throw RatingException.accessDenied();
        }
    }
}