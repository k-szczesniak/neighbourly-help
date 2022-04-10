package pl.lodz.p.ks.it.neighbourlyhelp.controller;

import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.ks.it.neighbourlyhelp.consistency.MessageSigner;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.AccessLevel;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.AccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.RegisterAccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.request.AccountPersonalDetailsDto;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.request.PasswordChangeRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.request.PasswordResetRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.endpoint.AccountEndpoint;
import pl.lodz.p.ks.it.neighbourlyhelp.endpoint.RoleEndpoint;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppOptimisticLockException;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.ConfirmationToken;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Email;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

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
    @ApiImplicitParam(name = "If-Match", value = "ETag", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class)
    public void grantAccessLevel(@NotNull @Email @PathVariable(name = "email") @Valid String email,
                                 @NotNull @PathVariable(name = "accessLevel") AccessLevel accessLevel) throws AppBaseException {
        roleEndpoint.grantAccessLevel(email, accessLevel);
    }

    @PatchMapping("/user/{email}/revoke/{accessLevel}")
    @Secured("ROLE_ADMIN")
    @ApiImplicitParam(name = "If-Match", value = "ETag", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class)
    public void revokeAccessLevel(@NotNull @Email @PathVariable(name = "email") @Valid String email,
                                  @NotNull @PathVariable(name = "accessLevel") AccessLevel accessLevel) throws AppBaseException {
        roleEndpoint.revokeAccessLevel(email, accessLevel);
    }

    @PatchMapping("/{email}/block")
    @Secured("ROLE_ADMIN")
    @ApiImplicitParam(name = "If-Match", value = "ETag", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class)
    public void blockAccount(@NotNull @Email @PathVariable("email") @Valid String email) throws AppBaseException {
        accountEndpoint.blockAccount(email);
    }

    @PatchMapping("/{email}/unblock")
    @Secured("ROLE_ADMIN")
    @ApiImplicitParam(name = "If-Match", value = "ETag", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class)
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

    @GetMapping("/user/{email}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<AccountDto> getAccountInformationByEmail(@NotNull @Email @PathVariable("email") @Valid String email) {
        AccountDto accountInfo = accountEndpoint.getAccountInfo(email);
        return ResponseEntity.ok()
                .eTag(messageSigner.sign(accountInfo))
                .body(accountInfo);
    }

    @PutMapping("/edit")
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    @ApiImplicitParam(name = "If-Match", value = "ETag", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class)
    public void editOwnAccountDetails(@NotNull @Valid @RequestBody AccountPersonalDetailsDto accountPersonalDetailsDto)
            throws AppBaseException {
        accountEndpoint.editOwnAccountDetails(accountPersonalDetailsDto);
    }

    @PutMapping("/edit/{email}")
    @Secured("ROLE_ADMIN")
    @ApiImplicitParam(name = "If-Match", value = "ETag", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class)
    public void editOtherAccountDetails(@NotNull @Email @PathVariable("email") @Valid String email,
                                        @NotNull @Valid @RequestBody AccountPersonalDetailsDto accountPersonalDetailsDto)
            throws AppBaseException {
        accountEndpoint.editOtherAccountDetails(email, accountPersonalDetailsDto);
    }

    @PutMapping("/self/password")
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    @ApiImplicitParam(name = "If-Match", value = "ETag", required = false, allowEmptyValue = true, paramType = "header", dataTypeClass = String.class)
    public void changePassword(@NotNull @Valid PasswordChangeRequestDto passwordChangeDto) throws AppOptimisticLockException {
        accountEndpoint.changePassword(passwordChangeDto);
    }

    @PostMapping(value = "/user/reset")
    public void resetPassword(@NotNull @Valid PasswordResetRequestDto passwordResetDto) throws AppBaseException {
        accountEndpoint.resetPassword(passwordResetDto);
    }
}