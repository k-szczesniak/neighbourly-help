package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule.ContactNumber;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule.Email;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule.Firstname;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule.Lastname;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule.Password;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegisterAccountDto {

    @Firstname
    private final String firstName;

    @Lastname
    private final String lastName;

    @Email
    private final String email;

    @Password
    private final String password;

    @ContactNumber
    private String contactNumber;
}