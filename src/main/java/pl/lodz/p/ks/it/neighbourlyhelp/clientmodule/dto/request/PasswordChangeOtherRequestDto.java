package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Email;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Password;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeOtherRequestDto {

    @Email
    @NotNull
    private String email;

    @Password
    @NotNull
    @ToString.Exclude
    private String givenPassword;
}