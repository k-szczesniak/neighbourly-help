package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule.ConfirmationToken;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule.Password;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequestDto {

    @Password
    @NotNull
    @ToString.Exclude
    private String password;

    @ConfirmationToken
    @NotNull
    private String resetToken;
}