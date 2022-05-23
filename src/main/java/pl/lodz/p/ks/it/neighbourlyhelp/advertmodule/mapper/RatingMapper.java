package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Rating;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.RatingDto;

@Mapper
public interface RatingMapper {

    @Mapping(target = "createdBy", expression = "java(rating.getCreatedBy().getEmail())")
    @Mapping(target = "contractId", expression = "java(rating.getContract().getId())")
//    @Mapping(target = "comment", expression = "java(rating.isHidden() ? null : rating.getComment())")
        // TODO: 22.05.2022 to think about mapping of comment
    RatingDto toRatingDto(Rating rating);

}