package pl.lodz.p.ks.it.neighbourlyhelp.security.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import pl.lodz.p.ks.it.neighbourlyhelp.endpoint.AccountEndpoint;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;

@Component
@Log
@RequiredArgsConstructor
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final AccountEndpoint accountEndpoint;

    private final HttpServletRequest request;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        accountEndpoint.updateValidAuth(event.getAuthentication().getName(), request.getRemoteAddr(), Date.from(Instant.now()));
    }
}