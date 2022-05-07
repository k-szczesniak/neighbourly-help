package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint;

import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewCityDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.CityDto;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

import java.util.List;

public interface CityEndpoint {

    List<CityDto> getAllCities() throws AppBaseException;

    CityDto get(Long cityId) throws AppBaseException;

    void addCity(NewCityDto newCityDto) throws AppBaseException;

    void deleteCity(Long cityId, String ifMatch) throws AppBaseException;
}