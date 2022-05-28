package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.advertmodule.Description;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.advertmodule.Rating;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRatingDto {

    @NotNull
    private Long id;

    @NotNull
    @Rating
    private short rate;

    @Description
    private String comment;
}