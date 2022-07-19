package pl.lodz.p.ks.it.neighbourlyhelp.utils.email;

import com.sun.mail.util.MailConnectException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.EmailException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(propagation = Propagation.MANDATORY, noRollbackFor = MailConnectException.class)
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
            helper.setFrom("neighbourly@email.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException | MailSendException e) {
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

    @Override
    public void sendLockAccountEmail(Account account) throws AppBaseException {
        String lang = account.getLanguage();
        String lockContent = mailConfig.getContentForType(lang, MailConfig.MailType.LOCK_ACCOUNT, account.getEmail());
        String lockSubject = mailConfig.getSubjectForType(lang, MailConfig.MailType.LOCK_ACCOUNT);
        send(account.getEmail(), lockSubject, lockContent);
    }

    @Override
    public void sendUnlockAccountEmail(Account account) throws AppBaseException {
        String lang = account.getLanguage();
        String lockContent = mailConfig.getContentForType(lang, MailConfig.MailType.UNLOCK_ACCOUNT, account.getEmail());
        String lockSubject = mailConfig.getSubjectForType(lang, MailConfig.MailType.UNLOCK_ACCOUNT);
        send(account.getEmail(), lockSubject, lockContent);
    }

    @Override
    public void sendResetPasswordEmail(Account account, String resetPasswordLink) throws AppBaseException {
        String lang = account.getLanguage();
        String resetContent = mailConfig.getContentForType(lang, MailConfig.MailType.RESET_PASSWORD, account.getEmail(),
                wrapCode(resetPasswordLink, mailConfig.getMailEndpointPasswordReset()));
        String resetSubject = mailConfig.getSubjectForType(lang, MailConfig.MailType.RESET_PASSWORD);
        send(account.getEmail(), resetSubject, resetContent);
    }

    /**
     * Wysyła wiadomość email z informacją o usunięciu nieaktywowanego konta użytkownika.
     *
     * @param account odbiorca wiadomości.
     * @throws AppBaseException wysyłanie wiadomości email nie powiodło się.
     */
    public void sendDeleteUnconfirmedAccountEmail(Account account) throws AppBaseException {
        String lang = account.getLanguage();
        String deleteUnconfirmedContent = mailConfig.getContentForType(lang, MailConfig.MailType.DELETE_UNCONFIRMED, account.getEmail());
        String deleteUnconfirmedSubject = mailConfig.getSubjectForType(lang, MailConfig.MailType.DELETE_UNCONFIRMED);
        send(account.getEmail(), deleteUnconfirmedSubject, deleteUnconfirmedContent);
    }

    @Override
    public void sendCreateContractEmail(Account account, Long contractId, String advertTitle) throws AppBaseException {
        String lang = account.getLanguage();
        String createContractContent = mailConfig.getContentForType(lang, MailConfig.MailType.CREATE_CONTRACT,
                account.getEmail(), contractId.toString(), advertTitle);
        String createContractSubject = mailConfig.getSubjectForType(lang, MailConfig.MailType.CREATE_CONTRACT);
        send(account.getEmail(), createContractSubject, createContractContent);
    }

    @Override
    public void sendCancelContractEmail(Account account, Long contractId, String advertTitle) throws AppBaseException {
        String lang = account.getLanguage();
        String cancelContractContent = mailConfig.getContentForType(lang, MailConfig.MailType.CANCEL_CONTRACT,
                account.getEmail(), contractId.toString(), advertTitle);
        String cancelContractSubject = mailConfig.getSubjectForType(lang, MailConfig.MailType.CANCEL_CONTRACT);
        send(account.getEmail(), cancelContractSubject, cancelContractContent);
    }

    @Override
    public void sendWaitingToApproveContractEmail(Account executor, Account publisher, Long contractId, String advertTitle) throws AppBaseException {
        String executorLang = executor.getLanguage();
        String executorToApproveContractContent = mailConfig.getContentForType(executorLang, MailConfig.MailType.EXECUTOR_TO_APPROVE_CONTRACT,
                executor.getEmail(), contractId.toString(), advertTitle);
        String executorToApproveContractSubject = mailConfig.getSubjectForType(executorLang, MailConfig.MailType.EXECUTOR_TO_APPROVE_CONTRACT);
        send(executor.getEmail(), executorToApproveContractSubject, executorToApproveContractContent);

        String publisherLang = publisher.getLanguage();
        String publisherToApproveContractContent = mailConfig.getContentForType(publisherLang, MailConfig.MailType.PUBLISHER_TO_APPROVE_CONTRACT,
                publisher.getEmail(), contractId.toString(), advertTitle);
        String publisherToApproveContractSubject = mailConfig.getSubjectForType(publisherLang, MailConfig.MailType.PUBLISHER_TO_APPROVE_CONTRACT);
        send(publisher.getEmail(), publisherToApproveContractSubject, publisherToApproveContractContent);
    }

    private String wrapCode(String code, String endpoint) {
        return String.join("/", mailConfig.getMailUri(), endpoint, code);
    }
}