package pl.lodz.p.ks.it.neighbourlyhelp.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
@Slf4j
public class I18n {
    private static final String BUNDLE_MESSAGES = "messages";

    private final HttpServletRequest servletRequest;

    /**
     * Tłumaczy klucz na wiadomość w języku pobranym z kontenera
     *
     * @param key klucz wiadomości
     * @return znajdująca się pod danym kluczem
     */
    public String getMessage(String key) {
        try {
            return getMessage(servletRequest.getLocale(), key);
        } catch (NullPointerException | MissingResourceException | ClassCastException e) {
            log.warn("Exception during translation of '" + key + "' :: " + e.getMessage());
        }
        return key;
    }

    /**
     * Tłumaczy klucz na wiadomość w języku przekazanym jako parametr
     *
     * @param locale język tłumaczenia
     * @param key klucz wiadomości
     * @return znajdująca się pod danym kluczem
     */
    public String getMessage(Locale locale, String key) {
        try {
            Locale currentLocale = Locale.getDefault();
            Locale.setDefault(locale);
            String message = getBundle(locale).getString(key);
            Locale.setDefault(currentLocale);
            return message;
        } catch (NullPointerException | MissingResourceException | ClassCastException e) {
            log.warn("Exception during translation of '" + key + "' :: " + e.getMessage());
        }
        return key;
    }

    /**
     * Tworzy paczkę językową dla przekazanego języka
     *
     * @param locale język tłumaczenia
     * @return paczka językowa
     */
    private ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle(BUNDLE_MESSAGES, locale);
    }
}