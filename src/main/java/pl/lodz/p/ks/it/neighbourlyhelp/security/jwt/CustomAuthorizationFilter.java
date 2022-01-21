package pl.lodz.p.ks.it.neighbourlyhelp.security.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.JwtTokenMalformedException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.JwtTokenMissingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Log
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenVerifier tokenVerifier;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(AUTHORIZATION);

        final List<String> permitAllEndpoints = List.of("/register", "/login");
        Predicate<HttpServletRequest> isApiSecured = r -> permitAllEndpoints.stream()
                .noneMatch(uri -> r.getServletPath().contains(uri));

        if (authorizationHeader == null ||
                !authorizationHeader.startsWith(jwtUtil.getTokenPrefix()) ||
                isApiSecured.test(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.replace(jwtUtil.getTokenPrefix(), "");

        try {
            tokenVerifier.validateToken(token);
        } catch (JwtTokenMalformedException | JwtTokenMissingException e) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            log.info(e.getMessage());
        }

        Claims body = tokenVerifier.getClaims(token);
        String username = body.getSubject();

        var authorities = (List<Map<String, String>>) body.get("authorities");

        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                .collect(Collectors.toSet());

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);


        filterChain.doFilter(request, response);
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Claims claims = tokenVerifier.getClaims(token);
        exchange.getRequest().mutate()
                .header("iat", String.valueOf(claims.get("iat")))
                .header("roles", String.valueOf(claims.get("authorities")))
                .build();
    }
}