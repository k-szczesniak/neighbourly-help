package pl.lodz.p.ks.it.neighbourlyhelp.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.AccessLevel;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.service.RoleService;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class RoleEndpointImpl implements RoleEndpoint {

    private final RoleService roleService;

    @Override
    public void revokeAccessLevel(String email, AccessLevel accessLevel) throws AppBaseException {
        // TODO: 10.02.2022 add logic
        roleService.revokeAccessLevel(email, accessLevel);
    }

    @Override
    public void grantAccessLevel(String email, AccessLevel accessLevel) throws AppBaseException {
        // TODO: 10.02.2022 add logic
        roleService.grantAccessLevel(email, accessLevel);
    }
}