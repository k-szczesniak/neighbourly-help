package pl.lodz.p.ks.it.neighbourlyhelp.exception;

public class CityException extends AppBaseException {

    private static final String CITY_NAME_INVALID = "exception.city.name_invalid";
    private static final String CITY_HAS_ADVERT_ASSIGNED = "exception.delete.city.has_advert";

    private CityException(String message) {
        super(message);
    }

    public static CityException cityNameExists() {
        return new CityException(CITY_NAME_INVALID);
    }

    public static CityException deleteHasAdvert() {
        return new CityException(CITY_HAS_ADVERT_ASSIGNED);
    }

}