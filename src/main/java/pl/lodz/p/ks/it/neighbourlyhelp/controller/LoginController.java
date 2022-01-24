package pl.lodz.p.ks.it.neighbourlyhelp.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.LoginCredentials;

@RequestMapping(path = "/auth")
@RestController
public class LoginController {

    @PostMapping("login")
    public void login(@RequestBody LoginCredentials loginCredentials) {

    }

}