package pl.lodz.p.ks.it.neighbourlyhelp.exception;

/**
 * Reprezentuje wyjątek dotyczący encji Advert
 */
public class AdvertException extends AppBaseException {

    private static final String ACCESS_DENIED = "exception.advert.access_denied";
    private static final String ADVERT_IS_IN_PROGRESS = "exception.advert.advert_is_in_progress";
    private static final String ADVERT_IS_DISAPPROVED = "exception.advert.advert_is_disapproved";

    protected AdvertException(String message, Throwable cause) {
        super(message, cause);
    }

    protected AdvertException(String message) {
        super(message);
    }

    /**
     * Wyjątek związany z brakień uprawnień do modyfikacji encji
     *
     * @return wyjątek AdvertException
     */
    public static AdvertException accessDenied() {
        return new AdvertException(ACCESS_DENIED);
    }

    /**
     * Wyjątek związany z faktem rezerwacji adverta
     *
     * @return wyjątek AdvertException
     */
    public static AdvertException advertIsInProgress() {
        return new AdvertException(ADVERT_IS_IN_PROGRESS);
    }

    /**
     * Wyjątek związany z faktem, że advert został usunięty
     *
     * @return wyjątek AdvertException
     */
    public static AdvertException advertIsDisapproved() {
        return new AdvertException(ADVERT_IS_DISAPPROVED);
    }

}