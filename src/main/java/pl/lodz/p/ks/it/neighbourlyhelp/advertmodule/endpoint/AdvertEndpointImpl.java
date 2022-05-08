package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Advert;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewAdvertRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.AdvertResponseDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.mapper.AdvertMapper;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.service.AdvertService;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AdvertEndpointImpl implements AdvertEndpoint {

    private final AdvertService advertService;

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public AdvertResponseDto get(Long advertId) throws AppBaseException {
        return Mappers.getMapper(AdvertMapper.class).toAdvertDto(advertService.get(advertId));
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public List<AdvertResponseDto> getAllAdverts() {
        AdvertMapper advertMapper = Mappers.getMapper(AdvertMapper.class);
        List<Advert> advertList = advertService.getAllAdverts();

        return advertList.stream().map(advertMapper::toAdvertDto).collect(Collectors.toList());
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public void addAdvert(NewAdvertRequestDto newAdvert) throws AppBaseException {
        Advert advert = new Advert();
        Mappers.getMapper(AdvertMapper.class).toAdvert(newAdvert, advert);
        advertService.addAdvert(advert, newAdvert.getCityId());
    }
}