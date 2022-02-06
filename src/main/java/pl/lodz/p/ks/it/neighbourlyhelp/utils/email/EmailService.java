package pl.lodz.p.ks.it.neighbourlyhelp.utils.email;

import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

public interface EmailService {

    void send(String to, String subject, String content) throws AppBaseException;

    void sendActivationSuccessEmail(Account account) throws AppBaseException;

    void sendActivationEmail(Account account, String activationLink) throws AppBaseException;
}