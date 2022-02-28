package pl.lodz.p.ks.it.neighbourlyhelp.endpoint;

import pl.lodz.p.ks.it.neighbourlyhelp.dto.response.AuthTokenResponseDto;

public interface AuthEndpoint {

    AuthTokenResponseDto refreshToken(String refreshToken);
}