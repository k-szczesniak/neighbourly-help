package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.endpoint;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response.AuthTokenResponseDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.service.AccountService;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppRuntimeException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.JwtTokenMalformedException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.JwtTokenMissingException;
import pl.lodz.p.ks.it.neighbourlyhelp.security.jwt.JwtUtil;
import pl.lodz.p.ks.it.neighbourlyhelp.security.jwt.TokenVerifier;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AuthEndpointImpl implements AuthEndpoint {

    private final AccountService accountService;
    private final TokenVerifier tokenVerifier;
    private final SecretKey secretKey;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;

    @Override
    public AuthTokenResponseDto refreshToken(String refreshToken) throws AppBaseException {
        try {
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

            return AuthTokenResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (JwtTokenMalformedException | JwtTokenMissingException e) {
            throw AppRuntimeException.jwtException(e);
        }
    }
}