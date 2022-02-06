package pl.lodz.p.ks.it.neighbourlyhelp.exception;


/**
 * Reprezentuje błąd dotyczący kodów aktywacyjnych
 */
public class ConfirmationTokenException extends AppBaseException {
    private static final String CODE_EXPIRED = "exception.code.code_expired";
    private static final String CODE_INVALID = "exception.code.code_invalid";
    private static final String CODE_USED = "exception.code.code_used";
    private static final String CODE_DUPLICATED = "exception.code.code_duplicated";

    private ConfirmationTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    private ConfirmationTokenException(String message) {
        super(message);
    }

    /**
     * Tworzy wyjątek reprezentujący nieaktualność kodu.
     * @return wyjątek CodeException
     */
    public static ConfirmationTokenException codeExpired() {
        return new ConfirmationTokenException(CODE_EXPIRED);
    }

    /**
     * Tworzy wyjątek reprezentujący nieprawidłowość przekazanego kodu.
     * @return wyjątek CodeException
     */
    public static ConfirmationTokenException codeInvalid() {
        return new ConfirmationTokenException(CODE_INVALID);
    }

    /**
     * Tworzy wyjątek reprezentujący użyty kod.
     * @return wyjątek CodeException
     */
    public static ConfirmationTokenException codeUsed() {
        return new ConfirmationTokenException(CODE_USED);
    }

    /**
     * Tworzy wyjątek reprezentujący duplikowany kod.
     * @return wyjątek CodeException
     */
    public static ConfirmationTokenException codeDuplicated(Throwable cause) {
        return new ConfirmationTokenException(CODE_DUPLICATED, cause);
    }
}