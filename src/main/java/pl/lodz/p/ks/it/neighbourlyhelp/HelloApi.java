package pl.lodz.p.ks.it.neighbourlyhelp;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class HelloApi {

    Random rand = new Random();

    @GetMapping("/api/rand")
    public String random() {
        return String.valueOf(rand.nextInt(100));
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @RolesAllowed("ROLE_ADMIN")
    @Secured({"ROLE_ADMIN", "ROLE_CLIENT"})
    @GetMapping("/api/ping")
    public String ping() {
        return "pong";
    }
}