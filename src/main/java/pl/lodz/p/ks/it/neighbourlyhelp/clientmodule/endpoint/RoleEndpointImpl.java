package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.endpoint;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.enums.AccessLevel;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response.RolesDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.service.AccountService;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.service.RoleService;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.mapper.IRoleMapper;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEndpoint;

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

    @Override
    public RolesDto getUserRole(String email) throws AppBaseException {
        return mapToRolesDto(accountService.getAccountByEmail(email));
    }

    private RolesDto mapToRolesDto(Account account) {
        return Mappers.getMapper(IRoleMapper.class).toRolesDto(account);
    }
}