package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint;

import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.EditAdvertRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewAdvertRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.AdvertDetailsResponseDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.AdvertResponseDto;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

import java.util.List;

public interface AdvertEndpoint {

    AdvertResponseDto get(Long advertId) throws AppBaseException;

    AdvertDetailsResponseDto getDetails(Long advertId) throws AppBaseException;

    List<AdvertResponseDto> getAllAdverts();

    List<AdvertResponseDto> getAllApprovedAdverts();

    List<AdvertResponseDto> getAllWaitingToApprove();

    void addAdvert(NewAdvertRequestDto newAdvert) throws AppBaseException;

    void deleteAdvert(Long advertId, String ifMatch) throws AppBaseException;

    void approveAdvert(Long advertId, String ifMatch) throws AppBaseException;

    void disapproveAdvert(Long advertId, String ifMatch) throws AppBaseException;

    void updateAdvert(EditAdvertRequestDto editedAdvert, String ifMatch) throws AppBaseException;

    List<AdvertResponseDto> getAllClientAdverts(Long userId) throws AppBaseException;

    List<AdvertResponseDto> getAllOwnAdverts() throws AppBaseException;
}