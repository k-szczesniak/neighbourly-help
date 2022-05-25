package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewRatingDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.UpdateRatingDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.RatingDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint.RatingHelper;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency.MessageSigner;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingHelper ratingHelper;

    private final MessageSigner messageSigner;

    @GetMapping("{id}")
    @Secured({"ROLE_MODERATOR", "ROLE_CLIENT"})
    public ResponseEntity<RatingDto> get(@PathVariable("id") Long ratingId) throws AppBaseException {
        RatingDto ratingDto = ratingHelper.get(ratingId);
        return ResponseEntity.ok()
                .eTag(messageSigner.sign(ratingDto))
                .body(ratingDto);
    }

    @GetMapping("all/{accountId}")
    @Secured({"ROLE_MODERATOR", "ROLE_CLIENT"})
    public List<RatingDto> getAllAccountRatings(@PathVariable("accountId") Long accountId) {
        return ratingHelper.getAllAccountRatings(accountId);
    }

    @PostMapping
    @Secured({"ROLE_CLIENT"})
    public void addRating(@NotNull @Valid @RequestBody NewRatingDto newRatingDto) throws AppBaseException {
        ratingHelper.addRating(newRatingDto);
    }

    @PutMapping
    @Secured({"ROLE_CLIENT"})
    public void updateRating(@RequestHeader("If-Match") String ifMatch,
                             @NotNull @Valid @RequestBody UpdateRatingDto updateRatingDto) throws AppBaseException {
        ratingHelper.updateRating(updateRatingDto, ifMatch);
    }

    @PatchMapping("/changeVisibility/{ratingId}")
    @Secured({"ROLE_MODERATOR"})
    public void changeVisibility(@RequestHeader("If-Match") String ifMatch,
                                   @PathVariable("ratingId") Long ratingId)
            throws AppBaseException {
        ratingHelper.changeVisibility(ratingId, ifMatch);
    }

    @DeleteMapping("{id}")
    @Secured({"ROLE_CLIENT"})
    public void deleteRating(@RequestHeader("If-Match") String ifMatch,
                             @NotNull @PathVariable("id") Long ratingId) throws AppBaseException {
        ratingHelper.deleteRating(ratingId, ifMatch);
    }

}