package pl.lodz.p.ks.it.neighbourlyhelp.security.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.endpoint.AccountEndpoint;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Date;

@Component
@Log
@RequiredArgsConstructor
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final AccountEndpoint accountEndpoint;

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        try {
            accountEndpoint.updateValidAuth(event.getAuthentication().getName(), request.getRemoteAddr(), Date.from(Instant.now()));
        } catch (AppBaseException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }
}