package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule.Email;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule.Password;

@NoArgsConstructor
@Getter
@Setter
public class LoginCredentials {

    @Email
    private String email;

    @Password
    private String password;
}