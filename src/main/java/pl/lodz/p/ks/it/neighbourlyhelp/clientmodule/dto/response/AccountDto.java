package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency.Signable;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto implements Signable {
    private String firstName;
    private String lastName;
    private String email;
    private String language;
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
