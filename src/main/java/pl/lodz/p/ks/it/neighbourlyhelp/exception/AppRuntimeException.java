package pl.lodz.p.ks.it.neighbourlyhelp.exception;

public class AppRuntimeException extends RuntimeException {

    private static final String JWT = "exception.runtime.jwt";

    public AppRuntimeException(String message) {
        super(message);
    }

    public AppRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public static AppRuntimeException jwtException(Throwable cause) {
        return new AppRuntimeException(JWT, cause);
    }
}