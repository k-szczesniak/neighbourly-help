package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency.Signable;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoyaltyPointsDto implements Signable {

    private Long id;

    private BigInteger totalPoints;

    private BigInteger blockedPoints;

    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d", version);
    }
}