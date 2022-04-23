package pl.lodz.p.ks.it.neighbourlyhelp;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.ks.it.neighbourlyhelp.entities.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.service.AccountService;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.email.EmailService;

import java.util.Random;

@RestController
@RequiredArgsConstructor
public class HelloApi {

    private final EmailService emailService;

    private final AccountService accountService;

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

    @GetMapping("/api/one")
    public String one() throws AppBaseException {
        emailService.send("adam@xyz.pl", "Confirmation", "Some text");
        return String.valueOf(rand.nextInt(100));
    }

    @GetMapping("/api/two")
    public String two() throws AppBaseException {
        Account account = accountService.getAccountByEmail("adam@nowak.pl");
        emailService.sendActivationSuccessEmail(account);
        return String.valueOf(rand.nextInt(100));
    }
}