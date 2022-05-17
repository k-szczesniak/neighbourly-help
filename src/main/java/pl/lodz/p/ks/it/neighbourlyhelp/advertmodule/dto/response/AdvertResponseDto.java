package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.enums.PreferableSettlement;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency.Signable;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvertResponseDto implements Signable {

    private Long id;

    private String title;

    private Date publicationDate;

    private String description;

    private String category;

    private Long publisherId;

    private Long cityId;

    private boolean approved;

    private Set<PreferableSettlement> preferableSettlementList;

    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d;%d;%s", id, version, category);
    }
}