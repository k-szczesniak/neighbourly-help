package pl.lodz.p.ks.it.neighbourlyhelp.service;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.entities.ClientData;
import pl.lodz.p.ks.it.neighbourlyhelp.entities.ConfirmationToken;
import pl.lodz.p.ks.it.neighbourlyhelp.entities.TokenType;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AccountException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.ConfirmationTokenException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.NotFoundException;
import pl.lodz.p.ks.it.neighbourlyhelp.repository.AccountRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.repository.ConfirmationTokenRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.email.EmailService;

import javax.annotation.security.PermitAll;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log
@Transactional(propagation = Propagation.MANDATORY)
public class AccountService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    @Secured("ROLE_ADMIN")
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

//    public void addAdminPermissions(String email) {
//        Account accountToUpdate = (Account) loadUserByUsername(email);
//        accountToUpdate.setAccessLevel(AccessLevel.ADMIN);
//        accountRepository.save(accountToUpdate);
//    }

    @PermitAll
    public void register(Account account) throws AppBaseException {
        accountRepository.findByEmail(account.getEmail()).ifPresent(userWithEmailExist -> {
            throw AccountException.emailExists();
        });
        accountRepository.findByContactNumber(account.getContactNumber()).ifPresent(userWithEmailExist -> {
            throw AccountException.contactNumberException();
        });

        String encodedPassword = bCryptPasswordEncoder.encode(account.getPassword());
        account.setPassword(encodedPassword);
        account.setCreatedBy(account);
        account.setEnabled(false);
        account.setLocked(false);

        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(UUID.randomUUID().toString())
                .used(false)
                .account(account)
                .tokenType(TokenType.ACCOUNT_ACTIVATION)
                .createdBy(account)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();

        account.getConfirmationTokenList().add(confirmationToken);
        accountRepository.saveAndFlush(account); // TODO: 06.02.2022 exception handling
        emailService.sendActivationEmail(account, confirmationToken.getToken());
    }

    public void confirmToken(String token) throws AppBaseException {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(NotFoundException::confirmationTokenNotFound);

        if (confirmationToken.getTokenType() != TokenType.ACCOUNT_ACTIVATION) {
            throw ConfirmationTokenException.codeInvalid();
        }
        if (confirmationToken.isUsed()) {
            throw ConfirmationTokenException.codeUsed();
        }
        Account account = confirmationToken.getAccount();
        if (account.isEnabled()) {
            throw AccountException.alreadyActivated();
        }
        // TODO: 06.02.2022 poprawic ponizsze walidacje
//        if (confirmationToken.getConfirmedAt() != null) {
//            log.info("Email already confirmed");
//            throw new IllegalStateException("email already confirmed");
//        }
//
//        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
//
//        if (expiredAt.isBefore(LocalDateTime.now())) {
//            log.info("Token expired");
//            throw new IllegalStateException("token expired");
//        }
//koniec walidacji

        ClientData clientData = new ClientData();
        clientData.setAccount(account);
        clientData.setCreatedBy(account);
        account.getRoleList().add(clientData);
        account.setModifiedBy(account);

        account.setEnabled(true);
        confirmationToken.setUsed(true);
        confirmationToken.setModifiedBy(account);

        confirmationToken.setConfirmedAt(LocalDateTime.now());

        confirmationTokenRepository.save(confirmationToken);
        accountRepository.save(account);
        emailService.sendActivationSuccessEmail(account);
    }

}