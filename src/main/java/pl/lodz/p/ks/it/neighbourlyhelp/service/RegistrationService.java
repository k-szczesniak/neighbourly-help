package pl.lodz.p.ks.it.neighbourlyhelp.service;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.token.ConfirmationToken;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.RegisterAccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.RegistrationException;

import java.time.LocalDateTime;

@Log
@Service
@AllArgsConstructor
public class RegistrationService {

    private final AccountService accountService;
    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegisterAccountDto request) throws RegistrationException {
        String token = null;
//        try {
//            Account newAccount = new Account(request.getFirstName(), request.getLastName(), request.getEmail(),
//                    request.getPassword(), AccessLevel.USER);
//            token = accountService.singUpUser(newAccount);
//        } catch (IllegalStateException illegalStateException) {
//            log.info("Exception occurred: " + illegalStateException.getClass());
//            throw new RegistrationException(illegalStateException.getMessage());
//        }
        return token;
    }

    @Transactional
    public String confirmToken(String token) throws IllegalStateException {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            log.info("Email already confirmed");
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            log.info("Token expired");
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        accountService.enableUser(confirmationToken.getAccount().getEmail());
        return "confirmed";
    }
}
