package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint;

import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewRatingDto;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

public interface RatingHelper {

    void addRating(NewRatingDto newRatingDto) throws AppBaseException;
}