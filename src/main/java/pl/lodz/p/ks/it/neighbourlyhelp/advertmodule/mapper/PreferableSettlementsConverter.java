package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.mapper;

import org.apache.commons.lang3.StringUtils;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.enums.PreferableSettlement;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PreferableSettlementsConverter implements AttributeConverter<Set<PreferableSettlement>, String> {

    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(Set<PreferableSettlement> attribute) {
        return attribute.stream().map(Enum::name).collect(Collectors.joining(SPLIT_CHAR));
    }

    @Override
    public Set<PreferableSettlement> convertToEntityAttribute(String dbData) {
        return Arrays.stream(StringUtils.defaultString(dbData).split(SPLIT_CHAR))
                .filter(StringUtils::isNotEmpty)
                .map(PreferableSettlement::valueOf)
                .collect(Collectors.toSet());
    }
}