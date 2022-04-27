package pl.lodz.p.ks.it.neighbourlyhelp.utils.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.repository.AccountRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.email.EmailService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

@Log
@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
//@Secured("RUN_AS_SYSTEM")
public class CronTaskRunner {

    @Value("${confirmation.account.expirationTime}")
    private Integer confirmationCodeExpirationTime;

    private final AccountRepository accountRepository;
    private final EmailService emailService;

    @Scheduled(cron = "${cron.deleteUnverifiedAccount}")
    public void deleteUnverifiedAccounts() {
        Instant expirationInstant = Instant.now().minus(confirmationCodeExpirationTime, ChronoUnit.HOURS);
        Date expirationDate = Date.from(expirationInstant);

        try {
            List<Account> unverifiedAccountsToRemove = accountRepository.findUnverifiedBefore(expirationDate);
            for (Account account : unverifiedAccountsToRemove) {
                accountRepository.delete(account);
                emailService.sendDeleteUnconfirmedAccountEmail(account);
            }
        } catch (AppBaseException e) {
            log.log(Level.WARNING, "Error while deleting unconfirmed accounts by scheduler");
        }
    }
}