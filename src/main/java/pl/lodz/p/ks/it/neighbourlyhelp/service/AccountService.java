package pl.lodz.p.ks.it.neighbourlyhelp.service;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.entities.ConfirmationToken;
import pl.lodz.p.ks.it.neighbourlyhelp.entities.TokenType;
import pl.lodz.p.ks.it.neighbourlyhelp.repository.AccountRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log
public class AccountService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

//    public void addAdminPermissions(String email) {
//        Account accountToUpdate = (Account) loadUserByUsername(email);
//        accountToUpdate.setAccessLevel(AccessLevel.ADMIN);
//        accountRepository.save(accountToUpdate);
//    }

    public String singUpUser(Account account) {
        boolean userExists = accountRepository
                .findByEmail(account.getEmail())
                .isPresent();
        if (userExists) {
            log.info("Email already taken: " + account.getEmail());
            throw new IllegalStateException(String.format("Email: '%s' already taken", account.getEmail()));
        }

        String encodedPassword = bCryptPasswordEncoder.encode(account.getPassword());
        account.setPassword(encodedPassword);

        accountRepository.save(account);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, false, TokenType.ACCOUNT_ACTIVATION, account, account, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15));
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public int enableUser(String email) {
        return accountRepository.enableUser(email);
    }
}