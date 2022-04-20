package pl.lodz.p.ks.it.neighbourlyhelp.endpoint;

import org.springframework.security.access.annotation.Secured;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.AccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.RegisterAccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.request.AccountPersonalDetailsDto;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.request.PasswordChangeOtherRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.request.PasswordChangeRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.request.PasswordResetRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.entities.ThemeColor;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

import javax.annotation.security.PermitAll;
import java.util.Date;
import java.util.List;

public interface AccountEndpoint {

    @PermitAll
    void registerAccount(RegisterAccountDto registerAccountDto) throws AppBaseException;

    @PermitAll
    void confirmAccount(String token) throws AppBaseException;

    @Secured("ROLE_ADMIN")
    List<AccountDto> getAllAccounts() throws AppBaseException;

    //    @PreAuthorize("isAnonymous()")
    // TODO: 16.02.2022 repair security annotation
    void updateInvalidAuth(String email, String ipAddress, Date authDate) throws AppBaseException;

    //    @PreAuthorize("isAnonymous()")
    // TODO: 16.02.2022 repair security annotation
    void updateValidAuth(String email, String ipAddress, Date authDate);

    @Secured("ROLE_ADMIN")
    void blockAccount(String email, String ifMatch) throws AppBaseException;

    @Secured("ROLE_ADMIN")
    void unblockAccount(String email, String ifMatch) throws AppBaseException;

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    AccountDto getOwnAccountInfo();

    @Secured("ROLE_ADMIN")
    AccountDto getAccountInfo(String email);

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    void editOwnAccountDetails(AccountPersonalDetailsDto accountPersonalDetailsDto, String ifMatch) throws AppBaseException;

    void editOtherAccountDetails(String email, AccountPersonalDetailsDto accountPersonalDetailsDto, String ifMatch) throws AppBaseException;

    void changePassword(PasswordChangeRequestDto passwordChangeDto, String ifMatch) throws AppBaseException;

    void resetPassword(PasswordResetRequestDto passwordResetDto) throws AppBaseException;

    /**
     * Send reset password token for account with given email
     *
     * @param email address where reset password message will be sent
     * @throws AppBaseException when there will be problems with sent resetting password message
     */
    void sendResetPasswordRequest(String email) throws AppBaseException;

    void changeOtherPassword(PasswordChangeOtherRequestDto passwordChangeOtherDto, String ifMatch) throws AppBaseException;

    void editOwnLanguage(String language, String ifMatch) throws AppBaseException;

    void changeThemeColor(ThemeColor themeColor, String ifMatch) throws AppBaseException;
}