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
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.EditAdvertRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewAdvertRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.AdvertDetailsResponseDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.AdvertResponseDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint.AdvertEndpoint;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency.MessageSigner;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("advert")
@RequiredArgsConstructor
public class AdvertController {

    private final AdvertEndpoint advertEndpoint;

    private final MessageSigner messageSigner;

    @GetMapping("{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public ResponseEntity<AdvertResponseDto> get(@PathVariable("id") Long advertId) throws AppBaseException {
        AdvertResponseDto advertDto = advertEndpoint.get(advertId);
        return ResponseEntity.ok()
                .eTag(messageSigner.sign(advertDto))
                .body(advertDto);
    }

    @GetMapping("details/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public ResponseEntity<AdvertDetailsResponseDto> getDetails(@PathVariable("id") Long advertId) throws AppBaseException {
        AdvertDetailsResponseDto advertDto = advertEndpoint.getDetails(advertId);
        return ResponseEntity.ok()
                .eTag(messageSigner.sign(advertDto))
                .body(advertDto);
    }

    @GetMapping
    @Secured({"ROLE_ADMIN"})
    public List<AdvertResponseDto> getAll() {
        return advertEndpoint.getAllAdverts();
    }

    @GetMapping("approved")
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public List<AdvertResponseDto> getAllApprovedAdvertsToTake() {
        return advertEndpoint.getAllApprovedAdvertsToTake();
    }

    @GetMapping("waitingToApprove")
    @Secured({"ROLE_MODERATOR"})
    public List<AdvertResponseDto> getAllWaitingToApprove() {
        return advertEndpoint.getAllWaitingToApprove();
    }

    @GetMapping("own")
    @Secured({"ROLE_CLIENT"})
    public List<AdvertResponseDto> getAllOwnAdverts() throws AppBaseException {
        return advertEndpoint.getAllOwnAdverts();
    }

    @GetMapping("ofUser/{userId}")
    @Secured({"ROLE_CLIENT"})
    public List<AdvertResponseDto> getAllClientAdverts(@NotNull @PathVariable("userId") Long userId) throws AppBaseException {
        return advertEndpoint.getAllClientAdverts(userId);
    }

    @PostMapping
    @Secured({"ROLE_CLIENT"})
    public void addAdvert(@NotNull @Valid @RequestBody NewAdvertRequestDto newAdvert) throws AppBaseException {
        advertEndpoint.addAdvert(newAdvert);
    }

    @PutMapping
    @Secured({"ROLE_CLIENT"})
    public void editAdvert(@RequestHeader("If-Match") String ifMatch,
                           @NotNull @Valid @RequestBody EditAdvertRequestDto editedAdvert) throws AppBaseException {
        advertEndpoint.updateAdvert(editedAdvert, ifMatch);
    }

    @PatchMapping("approve/{id}")
    @Secured({"ROLE_MODERATOR"})
    public void approveAdvert(@RequestHeader("If-Match") String ifMatch, @PathVariable("id") Long advertId) throws AppBaseException {
        advertEndpoint.approveAdvert(advertId, ifMatch);
    }

    @PatchMapping("disapprove/{id}")
    @Secured({"ROLE_MODERATOR"})
    public void disapproveAdvert(@RequestHeader("If-Match") String ifMatch, @PathVariable("id") Long advertId) throws AppBaseException {
        advertEndpoint.disapproveAdvert(advertId, ifMatch);
    }

    @DeleteMapping("{id}")
    @Secured({"ROLE_CLIENT"})
    public void deleteAdvert(@RequestHeader("If-Match") String ifMatch, @PathVariable("id") Long advertId) throws AppBaseException {
        advertEndpoint.deleteAdvert(advertId, ifMatch);
    }
}