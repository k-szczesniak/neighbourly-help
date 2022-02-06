package pl.lodz.p.ks.it.neighbourlyhelp.exception;

public class AccountException extends RuntimeException {
    private static final String ACCOUNT_EMAIL_INVALID = "exception.account.email_invalid";
    private static final String ACCOUNT_CONTACT_NUMBER = "exception.account.contact_number";
    private static final String ACCOUNT_ALREADY_ACTIVATED = "exception.account_already_activated.already_activated";

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
}