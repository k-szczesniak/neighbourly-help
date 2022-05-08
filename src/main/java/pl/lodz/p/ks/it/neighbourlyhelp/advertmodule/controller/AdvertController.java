package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewAdvertRequestDto;
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

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public List<AdvertResponseDto> getAll() throws AppBaseException {
        return advertEndpoint.getAllAdverts();
    }

    @PostMapping
    @Secured({"ROLE_CLIENT"})
    public void addAdvert(@NotNull @Valid @RequestBody NewAdvertRequestDto newAdvert) throws AppBaseException {
        advertEndpoint.addAdvert(newAdvert);
    }

}