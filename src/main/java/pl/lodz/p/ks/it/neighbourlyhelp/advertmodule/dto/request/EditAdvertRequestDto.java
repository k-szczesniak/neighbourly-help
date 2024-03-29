package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.enums.AdvertCategory;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.ValueOfEnum;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.advertmodule.Description;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.advertmodule.Title;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditAdvertRequestDto {

    @NotNull
    private Long id;

    @NotNull
    @Title
    private String title;

    @Description
    private String description;

    @ValueOfEnum(enumClass = AdvertCategory.class, message = "validation.advert.status.pattern")
    private String category;

    @NotNull
    @Min(value = 0)
    private BigInteger prize;

    @NotNull
    private Long cityId;
}