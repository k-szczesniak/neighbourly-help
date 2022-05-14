package pl.lodz.p.ks.it.neighbourlyhelp.exception;

public class CityException extends AppBaseException {

    private static final String CITY_NAME_INVALID = "exception.city.name_invalid";
    private static final String CITY_HAS_ADVERT_ASSIGNED = "exception.delete.city.has_advert";
    private static final String MODERATOR_ALREADY_HAS_CITY = "exception.city.moderator_has_already_city";

    private CityException(String message) {
        super(message);
    }

    public static CityException cityNameExists() {
        return new CityException(CITY_NAME_INVALID);
    }

    public static CityException deleteHasAdvert() {
        return new CityException(CITY_HAS_ADVERT_ASSIGNED);
    }

    public static CityException alreadyCityModerator() {
        return new CityException(MODERATOR_ALREADY_HAS_CITY);
    }
}