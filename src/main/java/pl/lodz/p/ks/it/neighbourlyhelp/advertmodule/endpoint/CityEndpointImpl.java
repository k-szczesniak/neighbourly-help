package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.City;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewCityDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.CityDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.mapper.ICityMapper;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.service.CityService;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEndpoint;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class CityEndpointImpl extends AbstractEndpoint implements CityEndpoint {

    private final CityService cityService;

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public List<CityDto> getAllCities() throws AppBaseException {
        ICityMapper cityMapper = Mappers.getMapper(ICityMapper.class);
        List<City> cityList = cityService.getAll();
        return cityList.stream().map(cityMapper::toCityDto).collect(Collectors.toList());
    }

    @Override
    public CityDto get(Long cityId) throws AppBaseException {
        return Mappers.getMapper(ICityMapper.class).toCityDto(cityService.get(cityId));
    }

    @Override
    @Secured({"ROLE_MODERATOR", "ROLE_ADMIN"})
    public void addCity(NewCityDto newCityDto) throws AppBaseException {
        ICityMapper cityMapper = Mappers.getMapper(ICityMapper.class);
        cityService.addCity(cityMapper.toCity(newCityDto));
    }

    @Override
    @Secured({"ROLE_ADMIN"})
    public void deleteCity(Long cityId, String ifMatch) throws AppBaseException {
        City city = cityService.get(cityId);

        CityDto cityIntegrity = Mappers.getMapper(ICityMapper.class).toCityDto(city);
        verifyIntegrity(cityIntegrity, ifMatch);

        cityService.deleteCity(city);
    }
}