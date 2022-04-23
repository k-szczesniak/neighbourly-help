package pl.lodz.p.ks.it.neighbourlyhelp.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.LoginCredentials;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response.AuthTokenResponseDto;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppRuntimeException;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final SecretKey secretKey;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginCredentials loginCredentials =
                    new ObjectMapper()
                            .readValue(request.getInputStream(), LoginCredentials.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginCredentials.getEmail(),
                    loginCredentials.getPassword());

            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new AppRuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String accessToken = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtUtil.getTokenExpiration()))
                .setIssuer(request.getRequestURL().toString())
                .signWith(secretKey)
                .compact();
        String refreshToken = Jwts.builder()
                .setSubject(authResult.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtUtil.getRefreshTokenExpiration()))
                .setIssuer(request.getRequestURL().toString())
                .signWith(secretKey)
                .compact();

        AuthTokenResponseDto authTokenResponseDto = AuthTokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), authTokenResponseDto);
    }
}