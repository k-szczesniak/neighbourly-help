package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.mapper;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Advert;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.enums.PreferableSettlement;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.EditAdvertRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewAdvertRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.AdvertResponseDto;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface AdvertMapper {

    @Mappings({
            @Mapping(target = "cityId", source = "city.id"),
            @Mapping(target = "contractId", source = "contract.id"),
            @Mapping(target = "publisherId", source = "publisher.id")
    })
    AdvertResponseDto toAdvertDto(Advert advert);

    void toAdvert(NewAdvertRequestDto newAdvert, @MappingTarget Advert advert);

    void toAdvert(EditAdvertRequestDto editedAdvert, @MappingTarget Advert advert);

    default Set<PreferableSettlement> toPreferableSettlementList(String value) {
        return Arrays.stream(StringUtils.defaultString(value).split(","))
                .filter(StringUtils::isNotEmpty)
                .map(PreferableSettlement::valueOf)
                .collect(Collectors.toSet());
    }
}