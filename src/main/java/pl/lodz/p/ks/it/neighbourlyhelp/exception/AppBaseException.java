package pl.lodz.p.ks.it.neighbourlyhelp.exception;

public class AppBaseException extends Exception {
    private static final String APP_BASE_EXCEPTION = "exception.app_error";

    public AppBaseException() {
        super(APP_BASE_EXCEPTION);
    }

    public AppBaseException(String message) {
        super(message);
    }

    public AppBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppBaseException(Throwable cause) {
        super(cause);
    }
}
