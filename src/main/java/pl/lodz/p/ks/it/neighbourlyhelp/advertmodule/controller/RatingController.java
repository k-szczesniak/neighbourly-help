package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewRatingDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint.RatingHelper;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingHelper ratingHelper;

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public void addRating(@NotNull @Valid @RequestBody NewRatingDto newRatingDto) throws AppBaseException {
        ratingHelper.addRating(newRatingDto);
    }
}