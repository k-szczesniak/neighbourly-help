package pl.lodz.p.ks.it.neighbourlyhelp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloApi {
    @GetMapping("/api/ping")
    public String ping() {
        return "pong";
    }
}