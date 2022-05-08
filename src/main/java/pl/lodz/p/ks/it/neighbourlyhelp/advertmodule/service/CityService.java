package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.City;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.repository.CityRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.service.AccountService;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.CityException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class CityService {

    private final CityRepository cityRepository;

    private final AccountService accountService;

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public List<City> getAll() throws AppBaseException {
        return cityRepository.findAll();
    }

    @Secured({"ROLE_ADMIN"})
    public City get(Long cityId) throws AppBaseException {
        return cityRepository.findById(cityId).orElseThrow(NotFoundException::cityNotFound);
    }

    @Secured({"ROLE_MODERATOR", "ROLE_ADMIN"})
    public void addCity(City newCity) throws AppBaseException {
        if (cityRepository.findByName(newCity.getName()).isPresent()) {
            throw CityException.cityNameExists();
        }

        newCity.setCreatedBy(accountService.getExecutorAccount());
        cityRepository.saveAndFlush(newCity);
    }

    @Secured({"ROLE_ADMIN"})
    public void deleteCity(City city) throws CityException {
        if(city.getAdvertList().size() != 0) {
            throw CityException.deleteHasAdvert();
        }
        cityRepository.delete(city);
    }
}