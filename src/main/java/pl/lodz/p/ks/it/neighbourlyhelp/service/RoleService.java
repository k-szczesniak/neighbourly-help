package pl.lodz.p.ks.it.neighbourlyhelp.service;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.AccessLevel;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.entities.AdminData;
import pl.lodz.p.ks.it.neighbourlyhelp.entities.ClientData;
import pl.lodz.p.ks.it.neighbourlyhelp.entities.ModeratorData;
import pl.lodz.p.ks.it.neighbourlyhelp.entities.Role;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.RoleException;
import pl.lodz.p.ks.it.neighbourlyhelp.repository.AccountRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.email.EmailService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log
public class RoleService {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final EmailService emailService;

    /**
     * Revoke access level of user.
     *
     * @param account     account entity
     * @param accessLevel access level
     * @throws AppBaseException when it was impossible to revoke the access level
     */
    @Secured("ROLE_ADMIN")
    public void revokeAccessLevel(Account account, AccessLevel accessLevel) throws AppBaseException {

        if (!account.isEnabled()) {
            throw RoleException.accountNotConfirmed();
        }

        Role role = account.getRoleList().stream()
                .filter(acc -> accessLevel.equals(acc.getAccessLevel()))
                .findFirst()
                .orElse(null);

        if (role == null || !role.isEnabled()) {
            throw RoleException.alreadyRevoked();
        }

        role.setModifiedBy(getEditorName());
        role.setEnabled(false);
        account.setRoleList(new HashSet<>(account.getRoleList()));
        accountRepository.save(account);
        emailService.sendDenyAccessLevelEmail(account, accessLevel.toString());
    }

    /**
     * Add access level to user.
     *
     * @param account     account entity
     * @param accessLevel access level
     * @throws AppBaseException when it was impossible to add the access level to user
     */
    @Secured("ROLE_ADMIN")
    public void grantAccessLevel(Account account, AccessLevel accessLevel) throws AppBaseException {

        if (!account.isEnabled()) {
            throw RoleException.accountNotConfirmed();
        }

        Set<AccessLevel> grantedRoles = account.getRoleList().stream()
                .filter(Role::isEnabled)
                .map(Role::getAccessLevel)
                .collect(Collectors.toSet());

        Role role = account.getRoleList().stream()
                .filter(x -> accessLevel.equals(x.getAccessLevel()))
                .findFirst()
                .orElse(null);

        if (role != null) {
            if (role.isEnabled()) {
                throw RoleException.alreadyGranted();
            } else {
                role.setModifiedBy(getEditorName());
            }
        } else {
            role = createUserRole(accessLevel);
            role.setAccount(account);
            role.setCreatedBy(getEditorName());
            account.getRoleList().add(role);
        }
        role.setEnabled(true);
        account.setRoleList(new HashSet<>(account.getRoleList()));
        accountRepository.save(account);
        emailService.sendGrantAccessLevelEmail(account, accessLevel.toString());
    }

    /**
     * Create user role object in condition of access level.
     *
     * @param accessLevel access level
     * @return user role object
     * @throws RoleException when access level does not exist
     */
    private Role createUserRole(AccessLevel accessLevel) throws RoleException {
        switch (accessLevel) {
            case ADMIN:
                return new AdminData();
            case MODERATOR:
                return new ModeratorData();
            case CLIENT:
                return new ClientData();
        }
        throw RoleException.unsupportedAccessLevel();
    }

    private Account getEditorName() throws AppBaseException {
        return accountService.getExecutorAccount();
    }

}