package pl.lodz.p.ks.it.neighbourlyhelp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.RolesDto;
import pl.lodz.p.ks.it.neighbourlyhelp.entities.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.entities.Role;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface IRoleMapper {

    @Mappings({
            @Mapping(target = "roleName", source = "accessLevel"),
            @Mapping(target = "version", source = "version")
    })
    RolesDto.SingleRoleDto toSingleRoleDto(Role role);

    default RolesDto toRolesDto(Account account) {
        List<RolesDto.SingleRoleDto> grantedRoleList = account.getRoleList().stream()
                .filter(Role::isEnabled)
                .map(this::toSingleRoleDto)
                .collect(Collectors.toList());
        List<RolesDto.SingleRoleDto> revokedRoleList = account.getRoleList().stream()
                .filter(role -> !role.isEnabled())
                .map(this::toSingleRoleDto)
                .collect(Collectors.toList());

        return new RolesDto(account.getId(), revokedRoleList, grantedRoleList);
    }
}