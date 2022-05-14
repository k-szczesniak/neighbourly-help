package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.enums.AccessLevel;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency.Signable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class RoleDto implements Signable {

    private Long id;
    private AccessLevel accessLevel;
    private boolean enabled;
    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d;%d", id, version);
    }
}