package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.LoginCredentials;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response.AuthTokenResponseDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.endpoint.AuthEndpoint;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthEndpoint authEndpoint;

    @PostMapping("login")
    public void login(@RequestBody LoginCredentials loginCredentials) {

    }

    @PostMapping("/token/refresh")
    public ResponseEntity<AuthTokenResponseDto> refreshToken(@NotNull @RequestBody String refreshToken) throws AppBaseException {
        AuthTokenResponseDto authTokenResponseDto = authEndpoint.refreshToken(refreshToken);
        return ResponseEntity.ok().body(authTokenResponseDto);
    }
}