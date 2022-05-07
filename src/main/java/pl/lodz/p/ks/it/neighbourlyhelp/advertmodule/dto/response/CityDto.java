package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency.Signable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDto implements Signable {

    private Long id;

    private String name;

    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d", version);
    }
}