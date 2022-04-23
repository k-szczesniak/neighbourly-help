package pl.lodz.p.ks.it.neighbourlyhelp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.entities.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.entities.ConfirmationToken;
import pl.lodz.p.ks.it.neighbourlyhelp.entities.TokenType;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.repository.ConfirmationTokenRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.email.EmailService;

import javax.annotation.security.PermitAll;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService emailService;

    @Value("${reset.expiration.minutes}")
    private Integer RESET_EXPIRATION_MINUTES;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.saveAndFlush(token);
    }

    public Optional<ConfirmationToken> getConfirmationToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    @PermitAll
    public void sendResetPasswordRequest(Account account) throws AppBaseException {
        List<ConfirmationToken> previousResetTokens = confirmationTokenRepository.findResetTokenByAccount(account);

        previousResetTokens.forEach(previousResetToken -> {
                    previousResetToken.setUsed(true);
                    confirmationTokenRepository.saveAndFlush(previousResetToken);
                });

        ConfirmationToken confirmationToken = prepareConfirmationToken(account);
        emailService.sendResetPasswordEmail(account, confirmationToken.getToken());
    }

    private ConfirmationToken prepareConfirmationToken(Account account) throws AppBaseException {
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(UUID.randomUUID().toString())
                .used(false)
                .account(account)
                .tokenType(TokenType.PASSWORD_RESET)
                .createdBy(account)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(RESET_EXPIRATION_MINUTES))
                .build();

        account.getConfirmationTokenList().add(confirmationToken);

        confirmationTokenRepository.saveAndFlush(confirmationToken);

        return confirmationToken;
    }
}