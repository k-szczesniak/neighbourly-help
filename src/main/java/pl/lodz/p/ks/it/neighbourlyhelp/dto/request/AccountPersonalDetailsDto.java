package pl.lodz.p.ks.it.neighbourlyhelp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.ContactNumber;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Firstname;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Lastname;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountPersonalDetailsDto {
    @Firstname
    @NotNull
    private String firstName;

    @Lastname
    @NotNull
    private String lastName;

    @ContactNumber
    private String contactNumber; // TODO: 22.02.2022 consider whether the account number should be modified
}