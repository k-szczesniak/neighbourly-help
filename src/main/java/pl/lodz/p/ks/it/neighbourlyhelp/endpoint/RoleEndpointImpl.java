package pl.lodz.p.ks.it.neighbourlyhelp.endpoint;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.AccessLevel;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.RolesDto;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppOptimisticLockException;
import pl.lodz.p.ks.it.neighbourlyhelp.mapper.IRoleMapper;
import pl.lodz.p.ks.it.neighbourlyhelp.service.AccountService;
import pl.lodz.p.ks.it.neighbourlyhelp.service.RoleService;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEndpoint;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class RoleEndpointImpl extends AbstractEndpoint implements RoleEndpoint {

    private final RoleService roleService;

    private final AccountService accountService;

    @Override
    @Secured("ROLE_ADMIN")
    public void revokeAccessLevel(String email, AccessLevel accessLevel, String ifMatch) throws AppBaseException {
        Account editAccount = accountService.getAccountByEmail(email);
        RolesDto rolesIntegrity = mapToRolesDto(editAccount);
        if(!verifyIntegrity(rolesIntegrity, ifMatch)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        roleService.revokeAccessLevel(editAccount, accessLevel);
    }

    @Override
    @Secured("ROLE_ADMIN")
    public void grantAccessLevel(String email, AccessLevel accessLevel, String ifMatch) throws AppBaseException {
        Account editAccount = accountService.getAccountByEmail(email);
        RolesDto rolesIntegrity = mapToRolesDto(editAccount);
        if(!verifyIntegrity(rolesIntegrity, ifMatch)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        roleService.grantAccessLevel(editAccount, accessLevel);
    }

    private RolesDto mapToRolesDto(Account account) {
        return Mappers.getMapper(IRoleMapper.class).toRolesDto(account);
    }
}