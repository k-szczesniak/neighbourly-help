package pl.lodz.p.ks.it.neighbourlyhelp.endpoint;

import org.springframework.security.access.annotation.Secured;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.AccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.RegisterAccountDto;
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
    void blockAccount(String email) throws AppBaseException;

    @Secured("ROLE_ADMIN")
    void unblockAccount(String email) throws AppBaseException;

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    AccountDto getOwnAccountInfo();
}