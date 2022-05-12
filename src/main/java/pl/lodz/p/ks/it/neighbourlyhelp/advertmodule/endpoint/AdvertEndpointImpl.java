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
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEndpoint;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AdvertEndpointImpl extends AbstractEndpoint implements AdvertEndpoint {

    private final AdvertService advertService;

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public AdvertResponseDto get(Long advertId) throws AppBaseException {
        return Mappers.getMapper(AdvertMapper.class).toAdvertDto(advertService.get(advertId));
    }

    @Override
    @Secured({"ROLE_ADMIN"})
    public List<AdvertResponseDto> getAllAdverts() {
        List<Advert> advertList = advertService.getAllAdverts();
        return toListOfAdvertResponseDto(advertList);
    }

    @Override
    public List<AdvertResponseDto> getAllApprovedAdverts() {
        List<Advert> advertList = advertService.getAllApprovedAdverts();
        return toListOfAdvertResponseDto(advertList);
    }

    @Override
    @Secured({"ROLE_MODERATOR"})
    public List<AdvertResponseDto> getAllWaitingToApprove() {
        List<Advert> advertList = advertService.getAllWaitingToApproveAdverts(getEmail());
        return toListOfAdvertResponseDto(advertList);
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public void addAdvert(NewAdvertRequestDto newAdvert) throws AppBaseException {
        Advert advert = new Advert();
        Mappers.getMapper(AdvertMapper.class).toAdvert(newAdvert, advert);
        advertService.addAdvert(advert, newAdvert.getCityId());
    }

    @Override
    @Secured({"ROLE_MODERATOR"})
    public void approveAdvert(Long advertId, String ifMatch) throws AppBaseException {
        Advert advertToApprove = advertService.get(advertId);

        AdvertResponseDto advertIntegrity = Mappers.getMapper(AdvertMapper.class).toAdvertDto(advertToApprove);
        verifyIntegrity(advertIntegrity, ifMatch);

        advertService.approveAdvert(advertToApprove, getEmail());
    }

    @Override
    @Secured({"ROLE_MODERATOR"})
    public void disapproveAdvert(Long advertId, String ifMatch) throws AppBaseException {
        Advert advertToDisapprove = advertService.get(advertId);

        AdvertResponseDto advertIntegrity = Mappers.getMapper(AdvertMapper.class).toAdvertDto(advertToDisapprove);
        verifyIntegrity(advertIntegrity, ifMatch);

        advertService.disapproveAdvert(advertToDisapprove, getEmail());
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public void deleteAdvert(Long advertId, String ifMatch) throws AppBaseException {
        Advert advertToDelete = advertService.get(advertId);

        AdvertResponseDto advertIntegrity = Mappers.getMapper(AdvertMapper.class).toAdvertDto(advertToDelete);
        verifyIntegrity(advertIntegrity, ifMatch);

        advertService.deleteAdvert(advertToDelete, getEmail());
    }

    private List<AdvertResponseDto> toListOfAdvertResponseDto(List<Advert> advertList) {
        AdvertMapper advertMapper = Mappers.getMapper(AdvertMapper.class);
        return advertList.stream().map(advertMapper::toAdvertDto).collect(Collectors.toList());
    }
}