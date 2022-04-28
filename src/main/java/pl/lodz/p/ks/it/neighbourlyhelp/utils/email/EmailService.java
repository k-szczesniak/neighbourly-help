package pl.lodz.p.ks.it.neighbourlyhelp.utils.email;

import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

public interface EmailService {

    void send(String to, String subject, String content) throws AppBaseException;

    void sendActivationSuccessEmail(Account account) throws AppBaseException;

    void sendActivationEmail(Account account, String activationLink) throws AppBaseException;

    void sendDenyAccessLevelEmail(Account account, String accessLevel) throws AppBaseException;

    void sendGrantAccessLevelEmail(Account account, String accessLevel) throws AppBaseException;

    void sendLockAccountEmail(Account account) throws AppBaseException;

    void sendUnlockAccountEmail(Account account) throws AppBaseException;

    void sendResetPasswordEmail(Account account, String resetPasswordLink) throws AppBaseException;

    void sendDeleteUnconfirmedAccountEmail(Account account) throws AppBaseException;
}