package pl.lodz.p.ks.it.neighbourlyhelp.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.JwtTokenMalformedException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.JwtTokenMissingException;

import javax.crypto.SecretKey;

@RequiredArgsConstructor
@Component
@Log
public class TokenVerifier {

    private final SecretKey secretKey;

    public void validateToken(final String token) throws JwtTokenMalformedException, JwtTokenMissingException {
        try {
            parseJwt(token);
        } catch (SignatureException ex) {
            throw new JwtTokenMalformedException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new JwtTokenMalformedException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new JwtTokenMalformedException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new JwtTokenMalformedException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new JwtTokenMissingException("JWT claims string is empty.");
        }
    }

    public Claims getClaims(final String token) {
        try {
            Jws<Claims> claimsJws = parseJwt(token);
            return claimsJws.getBody();
        } catch (Exception e) {
            log.info(e.getMessage() + " => " + e);
        }
        return null;
    }

    private Jws<Claims> parseJwt(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }
}