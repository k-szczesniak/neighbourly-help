package pl.lodz.p.ks.it.neighbourlyhelp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Email;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Password;

@NoArgsConstructor
@Getter
@Setter
public class LoginCredentials {
    @Email
    private String email;
    @Password
    private String password;
}
