package pl.lodz.p.ks.it.neighbourlyhelp;

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

    @GetMapping("/api/ping")
    public String ping() {
        return "pong";
    }
}