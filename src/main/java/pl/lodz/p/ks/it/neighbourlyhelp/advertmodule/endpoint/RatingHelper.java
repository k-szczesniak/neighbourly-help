package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint;

import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewRatingDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.UpdateRatingDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.RatingDto;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

import java.util.List;

public interface RatingHelper {

    RatingDto get(Long ratingId) throws AppBaseException;

    List<RatingDto> getAllAccountRatings(Long accountId);

    void addRating(NewRatingDto newRatingDto) throws AppBaseException;

    void updateRating(UpdateRatingDto updateRatingDto, String ifMatch) throws AppBaseException;

    void changeVisibility(Long ratingId, String ifMatch) throws AppBaseException;

    void deleteRating(Long ratingId, String ifMatch) throws AppBaseException;
}