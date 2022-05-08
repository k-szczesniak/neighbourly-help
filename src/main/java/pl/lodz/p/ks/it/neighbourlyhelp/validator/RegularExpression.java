package pl.lodz.p.ks.it.neighbourlyhelp.validator;

public class RegularExpression {
    public static final String EMAIL = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String ROLE = "Client | Admin";
    public static final String FIRSTNAME = "^[A-ZĆŁÓŚŹŻ\\s]{1}[a-ząęćńóśłźż]+$";
    public static final String LASTNAME = "^[A-ZĆŁÓŚŹŻ\\s]{1}[a-ząęćńóśłźż]+$";
    public static final String CONTACT_NUMBER = "^[0-9\\+][0-9]{8,14}$";
    public static final String LANGUAGE_CODE = "[a-z]{2}";
    public static final String CONFIRMATION_TOKEN = "^[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}$";

    public static final String CITY_NAME = "^[A-ZĆŁÓŚŹŻ\\s]{1}[A-Za-ząęćńóśłźż \\-]+$";
    public static final String SIMPLY_CITY_NAME = "^[a-z \\-]+$";

    public static final String TITLE = "^[A-ZĆŁÓŚŹŻ\\s]{1}[A-Za-ząęćńóśłźż \\-]+$";
    public static final String DESCRIPTION = "^[A-Za-z0-9ĄĘĆŃÓŚŁŹŻąęćńóśłźż.,:\\s\\-]+$";
}
