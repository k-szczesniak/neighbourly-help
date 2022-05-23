package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency.Signable;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDto implements Signable {

    private Long id;
    private short rate;
    private String comment;
    private Long contractId;
    private boolean hidden;
    private String createdBy;
    private Date creationDate;
    private Date modificationDate;

    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d;%s;%d", id, createdBy, version);
    }
}