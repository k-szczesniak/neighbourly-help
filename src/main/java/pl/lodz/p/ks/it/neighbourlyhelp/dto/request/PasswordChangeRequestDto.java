package pl.lodz.p.ks.it.neighbourlyhelp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Password;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeRequestDto {
    @Password
    @NotNull
    @ToString.Exclude
    private String oldPassword;
    @Password
    @NotNull
    @ToString.Exclude
    private String newPassword;
}