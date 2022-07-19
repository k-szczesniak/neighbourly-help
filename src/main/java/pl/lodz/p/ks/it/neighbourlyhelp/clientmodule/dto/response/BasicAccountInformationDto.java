package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency.Signable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasicAccountInformationDto implements Signable {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d;%d", id, version);
    }
}