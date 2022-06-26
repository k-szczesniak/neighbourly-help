package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.endpoint;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.ModeratorData;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.enums.AccessLevel;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response.BasicAccountInformationDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response.ModeratorDataDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response.RolesDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.service.AccountService;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.service.RoleService;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.mapper.AccountMapper;
import pl.lodz.p.ks.it.neighbourlyhelp.mapper.RoleMapper;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEndpoint;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = AppBaseException.class)
public class RoleEndpointImpl extends AbstractEndpoint implements RoleEndpoint {

    private final RoleService roleService;

    private final AccountService accountService;

    @Override
    @Secured("ROLE_ADMIN")
    public void revokeAccessLevel(String email, AccessLevel accessLevel, String ifMatch) throws AppBaseException {
        Account editAccount = accountService.getAccountByEmail(email);
        RolesDto rolesIntegrity = mapToRolesDto(editAccount);
        verifyIntegrity(rolesIntegrity, ifMatch);

        roleService.revokeAccessLevel(editAccount, accessLevel);
    }

    @Override
    @Secured("ROLE_ADMIN")
    public void grantAccessLevel(String email, AccessLevel accessLevel, String ifMatch) throws AppBaseException {
        Account editAccount = accountService.getAccountByEmail(email);
        RolesDto rolesIntegrity = mapToRolesDto(editAccount);
        verifyIntegrity(rolesIntegrity, ifMatch);

        roleService.grantAccessLevel(editAccount, accessLevel);
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public RolesDto getUserRole() throws AppBaseException {
        return mapToRolesDto(accountService.getExecutorAccount());
    }

    @Secured("ROLE_ADMIN")
    @Override
    public RolesDto getUserRole(String email) throws AppBaseException {
        return mapToRolesDto(accountService.getAccountByEmail(email));
    }

    @Override
    @Secured("ROLE_ADMIN")
    public ModeratorDataDto getModeratorData(String email) throws AppBaseException {
        ModeratorData moderatorData = roleService.getModeratorData(email);
        return Mappers.getMapper(RoleMapper.class).toModeratorDataDto(moderatorData);
    }

    @Override
    @Secured({"ROLE_ADMIN"})
    public List<BasicAccountInformationDto> getAllFreeModeratorsList() {
        AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);
        List<Account> allModeratorsList = roleService.getAllFreeModeratorsList();
        return allModeratorsList.stream().map(accountMapper::toBasicAccountInformationDto).collect(Collectors.toList());
    }

    private RolesDto mapToRolesDto(Account account) {
        return Mappers.getMapper(RoleMapper.class).toRolesDto(account);
    }
}