package pl.lodz.p.ks.it.neighbourlyhelp.endpoint;

import pl.lodz.p.ks.it.neighbourlyhelp.dto.response.AuthTokenResponseDto;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

public interface AuthEndpoint {

    AuthTokenResponseDto refreshToken(String refreshToken) throws AppBaseException;
}