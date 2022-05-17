package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency.Signable;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailContractDto implements Signable {

    private Long id;

    private Long advertId;

    private Date creationDate;

    private Date modificationDate;

    private Date startDate;

    private Date finishDate;

    private String contractStatus;

    private DetailContract_Executor executor;

    private Long loyaltyPointId;

    private Long ratingId;

    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d;%d;%d", id, executor.id, version);
    }

    @Data
    @AllArgsConstructor
    public static class DetailContract_Executor {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String contactNumber;
        private boolean locked;
    }
}