package pl.lodz.p.ks.it.neighbourlyhelp.exception;

/**
 * Reprezentuje błąd dotyczący encji Rating
 */
public class RatingException extends AppBaseException {
    private static final String RATING_ALREADY_EXISTS = "exception.rating_exception.rating_already_exists";
    private static final String CONTRACT_NOT_OWNED = "exception.rating_exception.contract_not_owned";
    private static final String CONTRACT_NOT_EXISTS = "exception.rating_exception.contract_not_exist";
    private static final String CONTRACT_NOT_FINISHED = "exception.rating_exception.contract_not_finished";
    private static final String ACCESS_DENIED = "exception.rating.access_denied";
    private static final String ALREADY_RATED = "exception.rating.already_rated";

    private RatingException(String message) {
        super(message);
    }

    private RatingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Wyjątek reprezentuje błąd związany z istnieniem już oceny dla danej rezerwacji.
     *
     * @return wyjątek RatingException
     */
    public static RatingException ratingAlreadyExists() {
        return new RatingException(RATING_ALREADY_EXISTS);
    }

    /**
     * Wyjątek reprezentuje błąd związany z próbą oceny nieposiadanej rezerwacji.
     *
     * @return wyjątek RatingException
     */
    public static RatingException contractNotOwned() {
        return new RatingException(CONTRACT_NOT_OWNED);
    }

    /**
     * Wyjątek reprezentuje błąd związany z próbą oceny nieistniejącej rezerwacji.
     *
     * @return wyjątek RatingException
     */
    public static RatingException contractNotExists() {
        return new RatingException(CONTRACT_NOT_EXISTS);
    }

    /**
     * Wyjątek reprezentuje błąd związany z próbą oceny niezakończonej rezerwacji.
     *
     * @return wyjątek RatingException
     */
    public static RatingException contractNotFinished() {
        return new RatingException(CONTRACT_NOT_FINISHED);
    }

    /**
     * Wyjątek reprezentuje błąd związany z próbą dostępu do zabronionego zasobu.
     *
     * @return wyjątek RatingException
     */
    public static RatingException accessDenied(){
        return new RatingException(ACCESS_DENIED);
    }

    /**
     * Wyjątek reprezentuje błąd związany z próbą dostępu do zabronionego zasobu.
     *
     * @return wyjątek RatingException
     */
    public static RatingException contractAlreadyRated(Throwable cause){
        return new RatingException(ALREADY_RATED, cause);
    }

}
