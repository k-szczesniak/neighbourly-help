package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint;

import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewRatingDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.UpdateRatingDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.RatingDto;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

public interface RatingHelper {

    RatingDto get(Long ratingId) throws AppBaseException;

    void addRating(NewRatingDto newRatingDto) throws AppBaseException;

    void updateRating(UpdateRatingDto updateRatingDto, String ifMatch) throws AppBaseException;
}