package pl.lodz.p.ks.it.neighbourlyhelp.security.configuration;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lodz.p.ks.it.neighbourlyhelp.security.jwt.JwtUtil;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
public class JwtSecretKey {

    private final JwtUtil jwtUtil;

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtUtil.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }
}