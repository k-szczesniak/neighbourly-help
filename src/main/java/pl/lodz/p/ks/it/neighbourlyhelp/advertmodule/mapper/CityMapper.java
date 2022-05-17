package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.mapper;

import org.mapstruct.Mapper;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.City;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewCityDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.CityDto;

@Mapper
public interface CityMapper {

    CityDto toCityDto(City city);

    City toCity(NewCityDto cityDto);

}