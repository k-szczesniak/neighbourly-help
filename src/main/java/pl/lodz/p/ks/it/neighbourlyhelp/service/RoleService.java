package pl.lodz.p.ks.it.neighbourlyhelp.service;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.AccessLevel;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.entities.Role;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.RoleException;
import pl.lodz.p.ks.it.neighbourlyhelp.repository.AccountRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;

@Service
@AllArgsConstructor
@Log
public class RoleService {

    private final AccountRepository accountRepository;

    private final AccountService accountService;

    private final HttpServletRequest servletRequest; // !!!!!!!!!!

    public void revokeAccessLevel(String email, AccessLevel accessLevel) throws AppBaseException {
        Account account = (Account) accountService.loadUserByUsername(email);

        if(!account.isEnabled()) {
            throw RoleException.accountNotConfirmed();
        }

        Role role = account.getRoleList().stream()
                .filter(acc -> accessLevel.equals(acc.getAccessLevel()))
                .findFirst()
                .orElse(null);

        if (role == null || !role.isEnabled()) {
            throw RoleException.alreadyRevoked();
        }

//        role.setModifiedBy((Account) accountService.loadUserByUsername(servletRequest.getUserPrincipal().getName())); //TODO: check what getUserPrincipal().getName() return
        role.setEnabled(false);
        account.setRoleList(new HashSet<>(account.getRoleList()));
        accountRepository.save(account);
//        TODO: send email
    }

}
