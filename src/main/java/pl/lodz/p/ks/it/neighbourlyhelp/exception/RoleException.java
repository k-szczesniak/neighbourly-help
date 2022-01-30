package pl.lodz.p.ks.it.neighbourlyhelp.exception;

public class RoleException extends AppBaseException {

    private static final String ALREADY_REVOKED = "exception.role_exception.already_revoked";
    private static final String ACCOUNT_NOT_CONFIRMED = "exception.role_exception.account_not_confirmed";

    public RoleException() {
    }

    public RoleException(String message) {
        super(message);
    }

    public RoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleException(Throwable cause) {
        super(cause);
    }

    /**
     * Tworzy wyjątek, gdy rola jest już odebrana użytkownikowi.
     *
     * @return wyjątek RoleException
     */
    public static RoleException alreadyRevoked() {
        return new RoleException(ALREADY_REVOKED);
    }

    /**
     * Tworzy wyjątek, gdy występuje próba zmiany roli dla konta, które nie jest potwierdzone.
     *
     * @return wyjątek RoleException
     */
    public static RoleException accountNotConfirmed() {
        return new RoleException(ACCOUNT_NOT_CONFIRMED);
    }
}
