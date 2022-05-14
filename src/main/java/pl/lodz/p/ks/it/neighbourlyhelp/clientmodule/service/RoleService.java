package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.City;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.AdminData;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.ClientData;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.ModeratorData;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Role;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.enums.AccessLevel;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.repository.AccountRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.repository.ModeratorDataRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.NotFoundException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.RoleException;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.email.EmailService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY, rollbackFor = AppBaseException.class)
public class RoleService {

    private final AccountRepository accountRepository;
    private final ModeratorDataRepository moderatorDataRepository;
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

    @Secured({"ROLE_ADMIN"})
    public ModeratorData getModeratorData(String moderatorEmail) throws AppBaseException {
        Account account = accountService.getAccountByEmail(moderatorEmail);

        Role moderatorRole = account.getRoleList().stream()
                .filter(Role::isEnabled)
                .filter(role -> role.getAccessLevel() == AccessLevel.MODERATOR)
                .findAny()
                .orElseThrow(NotFoundException::enabledModeratorRoleNotFound);

        return getById(moderatorRole.getId());
    }

    @Secured({"ROLE_ADMIN"})
    public ModeratorData findCityByModeratorEmail(String moderatorEmail) throws AppBaseException {
        City city = moderatorDataRepository.findCityByModeratorEmail(moderatorEmail)
                .orElseThrow(NotFoundException::moderatorAssignedCityNotFound);

        return city.getModeratorDataList().stream()
                .filter(manager -> manager.getAccount().getEmail().equals(moderatorEmail))
                .findAny()
                .orElseThrow(NotFoundException::moderatorAssignedCityNotFound);
    }

    @Secured({"ROLE_ADMIN"})
    public void deleteModeratorFromCity(ModeratorData moderatorData) throws AppBaseException {
        moderatorData.setCity(null);
        moderatorData.setModifiedBy(accountService.getExecutorAccount());

        moderatorDataRepository.saveAndFlush(moderatorData);
    }

    private ModeratorData getById(Long roleId) {
        return moderatorDataRepository.getById(roleId);
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