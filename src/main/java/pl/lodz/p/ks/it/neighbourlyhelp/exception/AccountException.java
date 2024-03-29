package pl.lodz.p.ks.it.neighbourlyhelp.exception;

public class AccountException extends AppBaseException {
    private static final String ACCOUNT_EMAIL_INVALID = "exception.account.email_invalid";
    private static final String ACCOUNT_CONTACT_NUMBER = "exception.account.contact_number";
    private static final String ACCOUNT_ALREADY_ACTIVATED = "exception.account_already_activated.already_activated";
    private static final String ACCOUNT_PASSWORDS_DONT_MATCH = "exception.passwords_dont_match.passwords_dont_match";
    private static final String ACCOUNT_NOT_CONFIRMED = "exception.account_not_confirmed.account_not_confirmed";
    private static final String ACCOUNT_LOCKED = "exception.account_locked.account_locked";
    private static final String ACCOUNT_THEME_ALREADY_SET = "exception.account.theme_already_set";

    public AccountException(String message) {
        super(message);
    }

    public AccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public static AccountException emailExists(Throwable cause) {
        return new AccountException(ACCOUNT_EMAIL_INVALID, cause);
    }

    public static AccountException emailExists() {
        return new AccountException(ACCOUNT_EMAIL_INVALID);
    }

    public static AccountException contactNumberException(Throwable cause) {
        return new AccountException(ACCOUNT_CONTACT_NUMBER, cause);
    }

    public static AccountException contactNumberException() {
        return new AccountException(ACCOUNT_CONTACT_NUMBER);
    }

    /**
     * Tworzy wyjątek reprezentujący niepowodzenie operacji ze względu na to, że konto użytkownika zostało wcześniej już aktywowane.
     *
     * @return wyjątek AccountException
     */
    public static AccountException alreadyActivated() {
        return new AccountException(ACCOUNT_ALREADY_ACTIVATED);
    }

    /**
     * Tworzy wyjątek reprezentujący niepowodzenie operacji ze względu na to, że podane nowe hasła nie są identyczne.
     *
     * @return wyjątek AccountException
     */
    public static AccountException passwordsDontMatch() {
        return new AccountException(ACCOUNT_PASSWORDS_DONT_MATCH);
    }

    /**
     * Tworzy wyjątek reprezentujący niepowodzenie operacji ze względu na to, że konto użytkownika nie jest potwierdzone.
     *
     * @return wyjątek AccountException
     */
    public static AccountException accountNotConfirmed() {
        return new AccountException(ACCOUNT_NOT_CONFIRMED);
    }

    /**
     * Tworzy wyjątek reprezentujący niepowodzenie operacji ze względu na to, że konto użytkownika jest zablokowane.
     *
     * @return wyjątek AccountException
     */
    public static AccountException accountLocked() {
        return new AccountException(ACCOUNT_LOCKED);
    }

    /**
     * Tworzy wyjątek reprezentujący niepowodzenie zmiany koloru szablonu, ze względu, że zmiana już istnieje.
     *
     * @return wyjątek AccountException
     */
    public static AccountException themeAlreadySet() {
        return new AccountException(ACCOUNT_THEME_ALREADY_SET);
    }
}