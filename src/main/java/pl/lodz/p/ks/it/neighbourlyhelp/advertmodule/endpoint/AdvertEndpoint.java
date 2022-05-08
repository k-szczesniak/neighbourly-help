package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint;

import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewAdvertRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.AdvertResponseDto;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

import java.util.List;

public interface AdvertEndpoint {

    AdvertResponseDto get(Long advertId) throws AppBaseException;

    List<AdvertResponseDto> getAllAdverts();

    void addAdvert(NewAdvertRequestDto newAdvert) throws AppBaseException;
}