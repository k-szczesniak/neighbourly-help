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
public class NewRatingDto {

    @NotNull
    @Rating
    private Short rate;

    @Description
    private String comment;

    @NotNull
    private Long contractId;

}