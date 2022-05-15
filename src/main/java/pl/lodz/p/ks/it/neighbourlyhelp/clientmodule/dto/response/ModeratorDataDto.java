package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.enums.AccessLevel;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class ModeratorDataDto extends RoleDto {

    private String cityName;

    public ModeratorDataDto(Long id, AccessLevel accessLevel, boolean enabled, Long version, String cityName) {
        super(id, accessLevel, enabled, version);
        this.cityName = cityName;
    }
}