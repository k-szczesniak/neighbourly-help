package pl.lodz.p.ks.it.neighbourlyhelp;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Class for tests purposes, it is not necessary to business approach
 */
@Service
public class RoleBeanToTest {

    @Secured("ROLE_ADMIN")
    public String getUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return securityContext.getAuthentication().getName();
    }
}