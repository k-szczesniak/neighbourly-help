package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Contract;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.ContractDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.DetailContractDto;

@Mapper
public interface ContractMapper {

    @Mappings({
            @Mapping(target = "ratingId", source = "rating.id"),
            @Mapping(target = "loyaltyPointId", source = "loyaltyPoint.id"),
            @Mapping(target = "advertId", source = "advert.id"),
            @Mapping(target = "contractStatus", source = "status")
    })
    DetailContractDto toDetailContractDto(Contract contract);

    @Mappings({
            @Mapping(target = "publisher", source = "advert.publisher"),
            @Mapping(target = "contractStatus", source = "status")
    })
    ContractDto toContractDto(Contract contract);
}