package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Advert;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewAdvertRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.AdvertResponseDto;

@Mapper
public interface AdvertMapper {

    @Mappings({
            @Mapping(target = "cityId", source = "city.id"),
            @Mapping(target = "contractId", source = "contract.id"),
            @Mapping(target = "publisherId", source = "publisher.id")
    })
    AdvertResponseDto toAdvertDto(Advert advert);

    void toAdvert(NewAdvertRequestDto newAdvert, @MappingTarget Advert advert);
}