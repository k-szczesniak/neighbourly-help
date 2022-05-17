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
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.mapper.CityMapper;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.service.CityService;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.ModeratorData;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response.ModeratorDataDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.service.RoleService;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.mapper.RoleMapper;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEndpoint;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = AppBaseException.class)
public class CityEndpointImpl extends AbstractEndpoint implements CityEndpoint {

    private final CityService cityService;

    private final RoleService roleService;

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public List<CityDto> getAllCities() throws AppBaseException {
        CityMapper cityMapper = Mappers.getMapper(CityMapper.class);
        List<City> cityList = cityService.getAll();
        return cityList.stream().map(cityMapper::toCityDto).collect(Collectors.toList());
    }

    @Override
    public CityDto get(Long cityId) throws AppBaseException {
        return Mappers.getMapper(CityMapper.class).toCityDto(cityService.get(cityId));
    }

    @Override
    @Secured({"ROLE_MODERATOR", "ROLE_ADMIN"})
    public void addCity(NewCityDto newCityDto) throws AppBaseException {
        CityMapper cityMapper = Mappers.getMapper(CityMapper.class);
        cityService.addCity(cityMapper.toCity(newCityDto));
    }

    @Override
    @Secured({"ROLE_ADMIN"})
    public void deleteCity(Long cityId, String ifMatch) throws AppBaseException {
        City city = cityService.get(cityId);

        CityDto cityIntegrity = Mappers.getMapper(CityMapper.class).toCityDto(city);
        verifyIntegrity(cityIntegrity, ifMatch);

        cityService.deleteCity(city);
    }

    @Override
    @Secured({"ROLE_ADMIN"})
    public void addModeratorToCity(Long cityId, String moderatorEmail, String ifMatch) throws AppBaseException {
        ModeratorData moderatorData = roleService.getModeratorData(moderatorEmail);
        ModeratorDataDto moderatorDataDto = Mappers.getMapper(RoleMapper.class).toModeratorDataDto(moderatorData);

        verifyIntegrity(moderatorDataDto, ifMatch);

        cityService.addModeratorToCity(cityId, moderatorData);
    }

    @Override
    @Secured({"ROLE_ADMIN"})
    public void deleteModeratorFromCity(String moderatorEmail, String ifMatch) throws AppBaseException {
        ModeratorData moderatorData = roleService.findCityByModeratorEmail(moderatorEmail);
        ModeratorDataDto moderatorDataDto = Mappers.getMapper(RoleMapper.class).toModeratorDataDto(moderatorData);

        verifyIntegrity(moderatorDataDto, ifMatch);

        roleService.deleteModeratorFromCity(moderatorData);
    }
}