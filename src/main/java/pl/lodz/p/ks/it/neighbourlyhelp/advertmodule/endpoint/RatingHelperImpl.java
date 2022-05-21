package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewRatingDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.service.RatingService;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = AppBaseException.class)
public class RatingHelperImpl implements RatingHelper {

    private final RatingService ratingService;

    @Override
    @Secured({"ROLE_ADMIN"})
    public void addRating(NewRatingDto newRatingDto) throws AppBaseException {
        ratingService.addRating(newRatingDto);
    }
}