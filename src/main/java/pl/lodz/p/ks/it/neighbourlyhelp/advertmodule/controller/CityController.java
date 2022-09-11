package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewCityDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.CityDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint.CityEndpoint;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency.MessageSigner;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/city")
@RequiredArgsConstructor
public class CityController {

    private final CityEndpoint cityEndpoint;

    private final MessageSigner messageSigner;

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public List<CityDto> getAllCities() throws AppBaseException {
        return cityEndpoint.getAllCities();
    }

    @GetMapping("{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public ResponseEntity<CityDto> get(@PathVariable("id") Long cityId) throws AppBaseException {
        CityDto cityDto = cityEndpoint.get(cityId);
        return ResponseEntity.ok()
                .eTag(messageSigner.sign(cityDto))
                .body(cityDto);
    }

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public void addCity(@Valid @RequestBody NewCityDto newCityDto) throws AppBaseException {
        cityEndpoint.addCity(newCityDto);
    }

    @DeleteMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public void deleteCity(@RequestHeader("If-Match") String ifMatch, @PathVariable("id") Long cityId) throws AppBaseException {
        cityEndpoint.deleteCity(cityId, ifMatch);
    }

    @PatchMapping("/add/{moderatorEmail}/{cityId}")
    @Secured({"ROLE_ADMIN"})
    public void addModeratorToCity(@RequestHeader("If-Match") String ifMatch,
                                   @PathVariable("cityId") Long cityId,
                                   @PathVariable("moderatorEmail") String moderatorEmail)
            throws AppBaseException {
        cityEndpoint.addModeratorToCity(cityId, moderatorEmail, ifMatch);
    }

    @PatchMapping("/delete/{moderatorEmail}")
    @Secured({"ROLE_ADMIN"})
    public void deleteModeratorFromCity(@RequestHeader("If-Match") String ifMatch,
                                   @PathVariable("moderatorEmail") String moderatorEmail)
            throws AppBaseException {
        cityEndpoint.deleteModeratorFromCity(moderatorEmail, ifMatch);
    }
}