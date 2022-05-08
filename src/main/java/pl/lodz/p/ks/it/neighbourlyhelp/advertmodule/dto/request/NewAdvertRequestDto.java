package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.enums.AdvertCategory;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.ValueOfEnum;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.advertmodule.Description;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.advertmodule.Title;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewAdvertRequestDto {

    @NotNull
    @Title
    private String title;

    @Description
    private String description;

    @ValueOfEnum(enumClass = AdvertCategory.class, message = "validation.advert.status.pattern")
    private String category;

    @NotNull
    private Long cityId;

}