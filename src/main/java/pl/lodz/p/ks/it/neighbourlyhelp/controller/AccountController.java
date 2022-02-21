package pl.lodz.p.ks.it.neighbourlyhelp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.ks.it.neighbourlyhelp.consistency.MessageSigner;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.AccessLevel;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.AccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.RefreshTokenDto;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.RegisterAccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.endpoint.AccountEndpoint;
import pl.lodz.p.ks.it.neighbourlyhelp.endpoint.RoleEndpoint;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.JwtTokenMalformedException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.JwtTokenMissingException;
import pl.lodz.p.ks.it.neighbourlyhelp.security.jwt.JwtUtil;
import pl.lodz.p.ks.it.neighbourlyhelp.security.jwt.TokenVerifier;
import pl.lodz.p.ks.it.neighbourlyhelp.service.AccountService;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.ConfirmationToken;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Email;

import javax.annotation.security.PermitAll;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Log
public class AccountController {

    private final AccountService accountService;
    private final TokenVerifier tokenVerifier;
    private final SecretKey secretKey;
    private final JwtUtil jwtUtil;

    private final AccountEndpoint accountEndpoint;
    private final RoleEndpoint roleEndpoint;

    private final MessageSigner messageSigner;

    @PostMapping(value = "/register")
    @PermitAll
    public void registerAccount(RegisterAccountDto registerAccountDto) throws AppBaseException {
        accountEndpoint.registerAccount(registerAccountDto);
    }

    @PostMapping(value = "/confirm/{token}")
    @PermitAll
    public void confirm(@NotNull @ConfirmationToken @PathVariable(name = "token") @Valid String token) throws AppBaseException {
        accountEndpoint.confirmAccount(token);
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public List<AccountDto> getAllAccounts() throws AppBaseException {
        return accountEndpoint.getAllAccounts();
    }

    @PatchMapping("/user/{email}/grant/{accessLevel}")
    @Secured("ROLE_ADMIN")
    public void grantAccessLevel(@NotNull @Email @PathVariable(name = "email") @Valid String email,
                                 @NotNull @PathVariable(name = "accessLevel") AccessLevel accessLevel) throws AppBaseException {
        roleEndpoint.grantAccessLevel(email, accessLevel);
    }

    @PatchMapping("/user/{email}/revoke/{accessLevel}")
    @Secured("ROLE_ADMIN")
    public void revokeAccessLevel(@NotNull @Email @PathVariable(name = "email") @Valid String email,
                                  @NotNull @PathVariable(name = "accessLevel") AccessLevel accessLevel) throws AppBaseException {
        roleEndpoint.revokeAccessLevel(email, accessLevel);
    }

    @PatchMapping("/{email}/block")
    @Secured("ROLE_ADMIN")
    public void blockAccount(@NotNull @Email @PathVariable("email") @Valid String email) throws AppBaseException {
        accountEndpoint.blockAccount(email);
    }

    @PatchMapping("/{email}/unblock")
    @Secured("ROLE_ADMIN")
    public void unblockAccount(@NotNull @Email @PathVariable("email") @Valid String email) throws AppBaseException {
        accountEndpoint.unblockAccount(email);
    }

    @GetMapping("/user")
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public ResponseEntity<AccountDto> getAccountInformation() {
        AccountDto ownAccountInfo = accountEndpoint.getOwnAccountInfo();
        return ResponseEntity.ok()
                .eTag(messageSigner.sign(ownAccountInfo))
                .body(ownAccountInfo);
    }

    @PostMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response, @RequestBody RefreshTokenDto refreshTokenDto) throws IOException {
        if (refreshTokenDto != null) {
            try {
                String refreshToken = refreshTokenDto.getRefreshToken();
                tokenVerifier.validateToken(refreshToken);
                String email = tokenVerifier.getClaims(refreshToken).getSubject();
                UserDetails user = accountService.getAccountByEmail(email);
                String accessToken = Jwts.builder()
                        .setSubject(user.getUsername())
                        .claim("authorities", user.getAuthorities())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + jwtUtil.getTokenExpiration()))
                        .setIssuer(request.getRequestURL().toString())
                        .signWith(secretKey)
                        .compact();

                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken);

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (JwtTokenMalformedException | JwtTokenMissingException e) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
                log.info(e.getMessage());
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}