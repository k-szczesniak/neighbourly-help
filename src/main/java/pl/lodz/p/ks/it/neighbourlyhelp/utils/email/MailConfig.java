package pl.lodz.p.ks.it.neighbourlyhelp.utils.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.I18n;

import java.text.MessageFormat;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MailConfig {

    private final I18n i18n;

    private static final String MAIL_CONTENT_SCHEME = "mail.%s.content";
    private static final String MAIL_SUBJECT_SCHEME = "mail.%s.subject";

    @Getter
    @Value("${mail.endpoint.activate}")
    private String mailEndpointActivate;

    @Getter
    @Value("${mail.endpoint.password_reset}")
    private String mailEndpointPasswordReset;

    @Getter
    @Value("${mail.uri}")
    private String mailUri;

    /**
     * Zwraca temat wiadomości
     * @param language język wiadomości
     * @param type typ wiadomości
     * @return wygenerowany temat
     */
    public String getSubjectForType(String language, MailConfig.MailType type) {
        String mailType = String.format(MAIL_SUBJECT_SCHEME, type.getValue());
        return i18n.getMessage(new Locale(language), mailType);
    }

    /**
     * Zwraca zawartość wiadamości w zależności od parametrów
     * @param language określa język wiadomości
     * @param type określa typ wiadomości email
     * @param param param
     * @return wygenerowana wiadomość
     */
    public String getContentForType(String language, MailConfig.MailType type, String... param) {
        String mailType = String.format(MAIL_CONTENT_SCHEME, type.getValue());
        String pattern = i18n.getMessage(new Locale(language), mailType);
        return MessageFormat.format(pattern, (Object[]) param);
    }

    @AllArgsConstructor
    public enum MailType {
        GRANT_ACCESS("access_grant"), DENY_ACCESS("access_deny"),
        LOCK_ACCOUNT("lock"), UNLOCK_ACCOUNT("unlock"),
        RESET_PASSWORD("password_reset"),
        DELETE_UNCONFIRMED("delete"),
        ACTIVATE_ACCOUNT("activate"),
        ACTIVATE_SUCCESS("activate_success"),
        CANCEL_CONTRACT("cancel_contract"),
        CREATE_CONTRACT("create_contract");

        @Getter
        private final String value;
    }
}