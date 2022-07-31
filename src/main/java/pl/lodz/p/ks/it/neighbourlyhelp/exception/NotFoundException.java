package pl.lodz.p.ks.it.neighbourlyhelp.exception;

/**
 * Reprezentuje błąd pojawiający się w sytuacji nieznalezienia encji
 */
public class NotFoundException extends AppBaseException {

    private static final String ACCOUNT_NOT_FOUND = "exception.not_found_exception.account_not_found";
    private static final String CONFIRMATION_TOKEN_NOT_FOUND = "exception.not_found_exception.confirmation_token_not_found";
    private static final String CITY_NOT_FOUND = "exception.not_found_exception.city_not_found";
    private static final String ADVERT_NOT_FOUND = "exception.not_found_exception.advert_not_found";
    private static final String CONTRACT_NOT_FOUND = "exception.not_found_exception.contract_not_found";
    private static final String ENABLED_MODERATOR_ROLE_NOT_FOUND = "exception.not_found_exception.enabled_moderator_role_not_found";
    private static final String MODERATOR_ASSIGNED_CITY_NOT_FOUND = "exception.not_found_exception.moderator_assigned_city_not_found";
    private static final String RATING_NOT_FOUND = "exception.not_found_exception.rating_not_found";
    private static final String LOYALTY_POINT_NOT_FOUND = "exception.not_found_exception.loyalty_point_not_found";
    private static final String ENABLED_CLIENT_ROLE_NOT_FOUND = "exception.not_found_exception.enabled_client_role_not_found";

    private NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    private NotFoundException(String message) {
        super(message);
    }

    /**
     * Tworzy wyjątek reprezentujący nieznalezienie encji account.
     *
     * @param cause wyjątek, który zostanie opakowany
     * @return wyjątek NotFoundException
     */
    public static NotFoundException accountNotFound(Throwable cause) {
        return new NotFoundException(ACCOUNT_NOT_FOUND, cause);
    }

    /**
     * Tworzy wyjątek reprezentujący nieznalezienie encji account.
     *
     * @return wyjątek NotFoundException
     */
    public static NotFoundException accountNotFound() {
        return new NotFoundException(ACCOUNT_NOT_FOUND);
    }

    /**
     * Tworzy wyjątek reprezentujący nieznalezienie encji pending code.
     *
     * @param cause wyjątek, który zostanie opakowany
     * @return wyjątek NotFoundException
     */
    public static NotFoundException confirmationTokenNotFound(Throwable cause) {
        return new NotFoundException(CONFIRMATION_TOKEN_NOT_FOUND, cause);
    }

    public static NotFoundException confirmationTokenNotFound() {
        return new NotFoundException(CONFIRMATION_TOKEN_NOT_FOUND);
    }

    public static NotFoundException cityNotFound() {
        return new NotFoundException(CITY_NOT_FOUND);
    }

    public static NotFoundException advertNotFound() {
        return new NotFoundException(ADVERT_NOT_FOUND);
    }

    public static NotFoundException enabledModeratorRoleNotFound() {
        return new NotFoundException(ENABLED_MODERATOR_ROLE_NOT_FOUND);
    }

    public static NotFoundException moderatorAssignedCityNotFound() {
        return new NotFoundException(MODERATOR_ASSIGNED_CITY_NOT_FOUND);
    }

    public static NotFoundException contractNotFound() {
        return new NotFoundException(CONTRACT_NOT_FOUND);
    }

    public static NotFoundException ratingNotFound() {
        return new NotFoundException(RATING_NOT_FOUND);
    }

    public static NotFoundException loyaltyPointNotFound() {
        return new NotFoundException(LOYALTY_POINT_NOT_FOUND);
    }

    public static NotFoundException enabledClientRoleNotFound() {
        return new NotFoundException(ENABLED_CLIENT_ROLE_NOT_FOUND);
    }
}