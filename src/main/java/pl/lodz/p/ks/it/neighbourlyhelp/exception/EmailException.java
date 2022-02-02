package pl.lodz.p.ks.it.neighbourlyhelp.exception;

/**
 * Reprezentuje błąd pojawiający się podczas wysyłania wiadomości email
 */
// todo: translate javadoc to english
public class EmailException extends AppBaseException {
    private static final String EMAIL_NOT_SENT = "exception.email_exception.email_not_sent";

    private EmailException(String message, Throwable cause) {
        super(message, cause);
    }

    private EmailException(String message) {
        super(message);
    }

    /**
     * Tworzy wyjątek reprezentujący problem w wysłaniu maila.
     * @param cause wyjątek, który zostanie opakowany
     * @return wyjątek EmailException
     */
    public static EmailException emailNotSent(Throwable cause) {
        return new EmailException(EMAIL_NOT_SENT, cause);
    }
}