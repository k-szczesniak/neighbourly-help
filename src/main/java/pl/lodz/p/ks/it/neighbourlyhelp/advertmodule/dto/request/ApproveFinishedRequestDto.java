package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.enums.LoyaltyPointStatus;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.ValueOfEnum;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.advertmodule.Description;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproveFinishedRequestDto {

    @NotNull
    private Long contractId;

    @ValueOfEnum(enumClass = LoyaltyPointStatus.class, message = "validation.advert.loyaltyPointStatus.pattern")
    private String loyaltyPointStatus;

    @Description
    private String comment;
}