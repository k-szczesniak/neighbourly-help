package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.ClientData;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.ConfirmationToken;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.enums.ThemeColor;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.enums.TokenType;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.request.PasswordChangeRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.repository.AccountRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AccountException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.ConfirmationTokenException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.NotFoundException;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.email.EmailService;

import javax.annotation.security.PermitAll;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log
@Transactional(propagation = Propagation.MANDATORY, rollbackFor = AppBaseException.class)
public class AccountService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;
    private final UserDetailsServiceImpl userService;
    private final ConfirmationTokenService tokenService;

    @Value("${incorrectLoginAttemptsLimit}")
    private String INCORRECT_LOGIN_ATTEMPTS_LIMIT;

    // TODO: 12.02.2022 add permission annotation
    public Account getAccountByEmail(String email) throws AppBaseException {
        try {
            return (Account) userService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            throw NotFoundException.accountNotFound(e);
        }
    }

    // TODO: 15.05.2022 add permission annotation
    public Account getAccountById(Long accountId) throws AppBaseException {
        return accountRepository.findById(accountId).orElseThrow(NotFoundException::accountNotFound);
    }

    @Secured("ROLE_ADMIN") // TODO: 20.04.2022 exception when sth went wrong
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @PermitAll
    public void register(Account account) throws AppBaseException {
        if (accountRepository.findByEmail(account.getEmail()).isPresent()) {
            throw AccountException.emailExists();
        }
        if (accountRepository.findByContactNumber(account.getContactNumber()).isPresent()) {
            throw AccountException.contactNumberException();
        }

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

    @PermitAll
    public void confirmToken(String token) throws AppBaseException {
        ConfirmationToken confirmationToken = tokenService.getConfirmationToken(token)
                .orElseThrow(NotFoundException::confirmationTokenNotFound);

        if (confirmationToken.getTokenType() != TokenType.ACCOUNT_ACTIVATION) {
            throw ConfirmationTokenException.tokenInvalid();
        }
        if (confirmationToken.isUsed()) {
            throw ConfirmationTokenException.tokenUsed();
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

        accountRepository.saveAndFlush(account);
        emailService.sendActivationSuccessEmail(account);
    }

    //    @PermitAll
//    @PreAuthorize("isAnonymous()")
    // TODO: 16.02.2022 repair security annotation
    public void updateInvalidAuth(Account account, String ipAddress, Date authDate) throws AppBaseException {
        account.setLastFailedLoginIpAddress(ipAddress);
        account.setLastFailedLoginDate(authDate);
        int incorrectLoginAttempts = account.getFailedLoginAttemptsCounter() + 1;
        if (incorrectLoginAttempts == Integer.parseInt(INCORRECT_LOGIN_ATTEMPTS_LIMIT)) {
            account.setLocked(true);
            account.setModifiedBy(null);
            emailService.sendLockAccountEmail(account);
        }
        account.setFailedLoginAttemptsCounter(incorrectLoginAttempts);

        accountRepository.saveAndFlush(account);
    }

    public void updateValidAuth(Account account, String ipAddress, Date authDate, String lang) {
        account.setLastSuccessfulLoginIpAddress(ipAddress);
        account.setLastSuccessfulLoginDate(authDate);
        account.setFailedLoginAttemptsCounter(0);
        account.setLanguage(lang.substring(0, 2));

        accountRepository.saveAndFlush(account);
    }

    @Secured("ROLE_ADMIN")
    public void blockAccount(Account account) throws AppBaseException {
        account.setLocked(true);
        account.setFailedLoginAttemptsCounter(0);
        account.setModifiedBy(getExecutorAccount());

        accountRepository.saveAndFlush(account);
        emailService.sendLockAccountEmail(account);
    }

    @Secured("ROLE_ADMIN")
    public void unblockAccount(Account account) throws AppBaseException {
        account.setLocked(false);
        account.setFailedLoginAttemptsCounter(0);
        account.setModifiedBy(getExecutorAccount());

        accountRepository.saveAndFlush(account);
        emailService.sendUnlockAccountEmail(account);
    }

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public void editAccountDetails(Account account) throws AppBaseException {
        account.setModifiedBy(getExecutorAccount());
        accountRepository.saveAndFlush(account);
    }

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public void changePassword(Account account, PasswordChangeRequestDto passwordChangeDto) throws AppBaseException {
        if (!bCryptPasswordEncoder.matches(passwordChangeDto.getOldPassword(), account.getPassword())) {
            throw AccountException.passwordsDontMatch();
        }

        changePassword(account, passwordChangeDto.getNewPassword());
    }

    @PermitAll
    public void resetPassword(String password, String token) throws AppBaseException {
        ConfirmationToken resetToken = tokenService.getConfirmationToken(token)
                .orElseThrow(NotFoundException::confirmationTokenNotFound);
        Account account = getAccountByEmail(resetToken.getAccount().getEmail());

        if (!account.isEnabled()) {
            throw AccountException.accountNotConfirmed();
        }
        if (!account.isAccountNonLocked()) {
            throw AccountException.accountLocked();
        }
        if (!resetToken.getTokenType().equals(TokenType.PASSWORD_RESET)) {
            throw ConfirmationTokenException.tokenInvalid();
        }
        if (resetToken.isUsed()) {
            throw ConfirmationTokenException.tokenUsed();
        }

        LocalDateTime expirationDate = resetToken.getExpiresAt();
        if (expirationDate.isBefore(LocalDateTime.now())) {
            throw ConfirmationTokenException.tokenExpired();
        }

        resetToken.setUsed(true);
        resetToken.setModifiedBy(account);
        tokenService.saveConfirmationToken(resetToken);
        changePassword(account, password);
    }

    @PermitAll
    public void sendResetPasswordRequest(String email) throws AppBaseException {
        Account account = getAccountByEmail(email);

        if (!account.isEnabled()) {
            throw AccountException.accountNotConfirmed();
        }
        if (!account.isAccountNonLocked()) {
            throw AccountException.accountLocked();
        }

        tokenService.sendResetPasswordRequest(account);
    }

    @Secured("ROLE_ADMIN")
    public void changeOtherPassword(Account editAccount, String givenPassword) throws AppBaseException {
        tokenService.sendResetPasswordRequest(editAccount);
        changePassword(editAccount, givenPassword);
    }

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public void changeAccountLanguage(Account account, String language) {
        account.setLanguage(language);
        account.setModifiedBy(account);

        accountRepository.saveAndFlush(account);
    }

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public void changeThemeColor(Account account, ThemeColor themeColor) throws AppBaseException {
        if (account.getThemeColor().equals(themeColor)) {
            throw AccountException.themeAlreadySet();
        }

        account.setThemeColor(themeColor);
        account.setModifiedBy(account);

        accountRepository.saveAndFlush(account);
    }

    @Secured({"ROLE_CLIENT"})
    public void updateAccountRating(Long accountId, BigDecimal accountRating) throws AppBaseException {
        Account account = accountRepository.findById(accountId).orElseThrow(NotFoundException::accountNotFound);
        account.setRating(accountRating);

        accountRepository.saveAndFlush(account);
    }

    public Account getExecutorAccount() throws AppBaseException {
        return getAccountByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private void changePassword(Account account, String newPassword) throws AppBaseException {
        String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
        account.setPassword(encodedPassword);
        account.setModifiedBy(getExecutorAccount());

        accountRepository.saveAndFlush(account);
    }
}