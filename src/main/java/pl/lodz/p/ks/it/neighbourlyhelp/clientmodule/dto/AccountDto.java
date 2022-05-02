package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency.Signable;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule.ContactNumber;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule.Email;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule.Firstname;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule.Language;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule.Lastname;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto implements Signable {
    @Firstname
    private String firstName;
    @Lastname
    private String lastName;
    @Email
    private String email;
    @Language
    private String language;
    @ContactNumber
    private String contactNumber;
    private Boolean locked;
    private Boolean enabled;
    private Long version;
    private Date lastSuccessfulLoginDate;
    private String lastSuccessfulLoginIpAddress;
    private String themeColor;
    private Date lastFailedLoginDate;
    private String lastFailedLoginIpAddress;

    @Override
    public String getMessageToSign() {
        return String.format("%d", version);
    }
}
