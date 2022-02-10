package pl.lodz.p.ks.it.neighbourlyhelp.utils.email;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.EmailException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
@Slf4j
// todo: translate javadoc to english and move to interface
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private final MailConfig mailConfig;

    @Override
    @Async
    public void send(String to, String subject, String content) throws AppBaseException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom("hello@test.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw EmailException.emailNotSent(e);
        }
    }

    /**
     * Wysyła email z powiadomieniem o pomyślnym zakończeniu procesu aktywacji konta.
     *
     * @param account odbiorca wiadomości.
     * @throws AppBaseException wysyłanie wiadomości email nie powiodło się.
     */
    @Override
    public void sendActivationSuccessEmail(Account account) throws AppBaseException {
        String lang = account.getLanguage();
        String successContent = mailConfig.getContentForType(lang, MailConfig.MailType.ACTIVATE_SUCCESS, account.getEmail());
        String successSubject = mailConfig.getSubjectForType(lang, MailConfig.MailType.ACTIVATE_SUCCESS);
        send(account.getEmail(), successSubject, successContent);
    }

    @Override
    public void sendActivationEmail(Account account, String activationLink) throws AppBaseException {
        String lang = account.getLanguage();
        String activationContent = mailConfig.getContentForType(lang, MailConfig.MailType.ACTIVATE_ACCOUNT, account.getFirstName(),
                wrapCode(activationLink, mailConfig.getMailEndpointActivate()));
        String activationSubject = mailConfig.getSubjectForType(lang, MailConfig.MailType.ACTIVATE_ACCOUNT);
        send(account.getEmail(), activationSubject, activationContent);
    }

    @Override
    public void sendDenyAccessLevelEmail(Account account, String accessLevel) throws AppBaseException {
        String lang = account.getLanguage();
        String denyAccessContent = mailConfig.getContentForType(lang, MailConfig.MailType.DENY_ACCESS, account.getEmail(), accessLevel);
        String denyAccessSubject = mailConfig.getSubjectForType(lang, MailConfig.MailType.DENY_ACCESS);
        send(account.getEmail(), denyAccessSubject, denyAccessContent);
    }

    @Override
    public void sendGrantAccessLevelEmail(Account account, String accessLevel) throws AppBaseException {
        String lang = account.getLanguage();
        String grantAccessContent = mailConfig.getContentForType(lang, MailConfig.MailType.GRANT_ACCESS, account.getEmail(), accessLevel);
        String grantAccessSubject = mailConfig.getSubjectForType(lang, MailConfig.MailType.GRANT_ACCESS);
        send(account.getEmail(), grantAccessSubject, grantAccessContent);
    }

    private String wrapCode(String code, String endpoint) {
        return String.join("/", mailConfig.getMailUri(), endpoint, code);
    }
}