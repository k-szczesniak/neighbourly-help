package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.advertmodule.CityName;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.advertmodule.SimplyCityName;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO reprezentujaca tworzenie nowego miasta.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCityDto {
    @NotNull
    @CityName
    private String name;

    @NotNull
    @SimplyCityName
    private String simplyName;
}