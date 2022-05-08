package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Advert;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.repository.AdvertRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.service.AccountService;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.NotFoundException;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class AdvertService {

    private final AdvertRepository advertRepository;

    private final CityService cityService;

    private final AccountService accountService;

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public Advert get(Long advertId) throws AppBaseException {
        return advertRepository.findById(advertId).orElseThrow(NotFoundException::advertNotFound);
    }

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public List<Advert> getAllAdverts() {
        return advertRepository.findAll().stream()
                .filter(advert -> !advert.isDelete())
                .collect(Collectors.toList());
    }

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public List<Advert> getAllActiveAdverts() {
        return getAllAdverts().stream()
                .filter(advert -> advert.getContract() != null)
                .collect(Collectors.toList());
    }

    @Secured({"ROLE_CLIENT"})
    public void addAdvert(Advert advert, @NotNull Long cityId) throws AppBaseException {
        advert.setCity(cityService.get(cityId));
        Account publisher = accountService.getExecutorAccount();
        advert.setPublisher(publisher);
        advert.setCreatedBy(publisher);
        advert.setPublicationDate(new Date());
        advertRepository.saveAndFlush(advert);
    }
}