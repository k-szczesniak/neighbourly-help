package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.enums.AccessLevel;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.enums.ThemeColor;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.request.AccountPersonalDetailsDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.request.PasswordChangeOtherRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.request.PasswordChangeRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.request.PasswordResetRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.request.RegisterAccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response.AccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.endpoint.AccountEndpoint;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppRuntimeException;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency.MessageSigner;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule.ConfirmationToken;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule.Email;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule.Language;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountEndpoint accountEndpoint;

    private final MessageSigner messageSigner;

    @PostMapping(value = "/register")
    @PermitAll
    public void registerAccount(@NotNull @Valid @RequestBody RegisterAccountDto registerAccountDto) throws AppBaseException {
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

    @PatchMapping("/{email}/block")
    @Secured("ROLE_ADMIN")
    public void blockAccount(@RequestHeader("If-Match") String ifMatch,
                             @NotNull @Email @PathVariable("email") @Valid String email) throws AppBaseException {
        accountEndpoint.blockAccount(email, ifMatch);
    }

    @PatchMapping("/{email}/unblock")
    @Secured("ROLE_ADMIN")
    public void unblockAccount(@RequestHeader("If-Match") String ifMatch,
                               @NotNull @Email @PathVariable("email") @Valid String email) throws AppBaseException {
        accountEndpoint.unblockAccount(email, ifMatch);
    }

    @GetMapping("/user")
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public ResponseEntity<AccountDto> getAccountInformation() throws AppBaseException {
        AccountDto ownAccountInfo = accountEndpoint.getOwnAccountInfo();
        return ResponseEntity.ok()
                .eTag(messageSigner.sign(ownAccountInfo))
                .body(ownAccountInfo);
    }

    @GetMapping("/user/{email}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<AccountDto> getAccountInformationByEmail(@NotNull @Email @PathVariable("email") @Valid String email) throws AppBaseException {
        AccountDto accountInfo = accountEndpoint.getAccountInfo(email);
        return ResponseEntity.ok()
                .eTag(messageSigner.sign(accountInfo))
                .body(accountInfo);
    }

    @PutMapping("/edit")
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public void editOwnAccountDetails(@RequestHeader("If-Match") String ifMatch,
                                      @NotNull @Valid @RequestBody AccountPersonalDetailsDto accountPersonalDetailsDto)
            throws AppBaseException {
        accountEndpoint.editOwnAccountDetails(accountPersonalDetailsDto, ifMatch);
    }

    @PutMapping("/edit/{email}")
    @Secured("ROLE_ADMIN")
    public void editOtherAccountDetails(@RequestHeader("If-Match") String ifMatch,
                                        @NotNull @Email @PathVariable("email") @Valid String email,
                                        @NotNull @Valid @RequestBody AccountPersonalDetailsDto accountPersonalDetailsDto)
            throws AppBaseException {
        accountEndpoint.editOtherAccountDetails(email, accountPersonalDetailsDto, ifMatch);
    }

    @PutMapping("/self/password")
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public void changePassword(@RequestHeader("If-Match") String ifMatch,
                               @NotNull @Valid @RequestBody PasswordChangeRequestDto passwordChangeDto) throws AppBaseException {
        accountEndpoint.changePassword(passwordChangeDto, ifMatch);
    }

    @PostMapping(value = "/user/reset")
    public void resetPassword(@NotNull @Valid @RequestBody PasswordResetRequestDto passwordResetDto) throws AppBaseException {
        accountEndpoint.resetPassword(passwordResetDto);
    }

    @PutMapping("/user/{email}/reset")
    public void sendResetPasswordRequest(@NotNull @Email @PathVariable("email") @Valid String email) throws AppBaseException {
        accountEndpoint.sendResetPasswordRequest(email);
    }

    @PutMapping("/user/password")
    @Secured("ROLE_ADMIN")
    public void changeOtherPassword(@RequestHeader("If-Match") String ifMatch,
                                    @NotNull @Valid @RequestBody PasswordChangeOtherRequestDto passwordChangeOtherDto)
            throws AppBaseException, AppRuntimeException {
        accountEndpoint.changeOtherPassword(passwordChangeOtherDto, ifMatch);
    }

    @PatchMapping("/self/edit/language/{lang}")
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public void editOwnLanguage(@RequestHeader("If-Match") String ifMatch,
                                @NotNull @Language @PathVariable("lang") @Valid String language) throws AppBaseException {
        accountEndpoint.editOwnLanguage(language, ifMatch);
    }

    @PatchMapping("/theme/{themeColor}")
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public void changeThemeColor(@RequestHeader("If-Match") String ifMatch,
                                 @NotNull @PathVariable("themeColor") ThemeColor themeColor) throws AppBaseException {
        accountEndpoint.changeThemeColor(themeColor, ifMatch);
    }

    @GetMapping("changeOwnAccessLevel/{accessLevel}")
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public ResponseEntity<?> changeOwnAccessLevel(@NotNull @PathVariable("accessLevel") AccessLevel accessLevel) throws AppBaseException {
        accountEndpoint.changeOwnAccessLevel(accessLevel);
        return ResponseEntity.noContent().build();
    }
}