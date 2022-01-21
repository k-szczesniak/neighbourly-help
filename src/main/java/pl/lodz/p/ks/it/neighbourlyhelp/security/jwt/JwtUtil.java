package pl.lodz.p.ks.it.neighbourlyhelp.security.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.prefix}")
    private String tokenPrefix;

    @Value("${jwt.token.validity}")
    private long tokenExpiration;

    @Value("${jwt.refreshtoken.validity}")
    private long refreshTokenExpiration;

}