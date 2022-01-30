package pl.lodz.p.ks.it.neighbourlyhelp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Email;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Firstname;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Lastname;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    @Firstname
    private String firstName;
    @Lastname
    private String lastName;
    @Email
    private String email;
    private Boolean locked;
    private Boolean enabled;
}
