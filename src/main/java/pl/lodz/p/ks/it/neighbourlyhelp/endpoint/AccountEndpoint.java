package pl.lodz.p.ks.it.neighbourlyhelp.endpoint;

import pl.lodz.p.ks.it.neighbourlyhelp.dto.RegisterAccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

import javax.annotation.security.PermitAll;

public interface AccountEndpoint {

    @PermitAll
    void registerAccount(RegisterAccountDto registerAccountDto) throws AppBaseException;

    @PermitAll
    void confirmAccount(String token) throws AppBaseException;
}