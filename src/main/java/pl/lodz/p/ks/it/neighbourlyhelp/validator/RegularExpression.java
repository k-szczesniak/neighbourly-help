package pl.lodz.p.ks.it.neighbourlyhelp.validator;

public class RegularExpression {
    public static final String EMAIL = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String ROLE = "Client | Admin";
    public static final String FIRSTNAME = "^[A-ZĆŁÓŚŹŻ\\s]{1}[a-ząęćńóśłźż]+$";
    public static final String LASTNAME = "^[A-ZĆŁÓŚŹŻ\\s]{1}[a-ząęćńóśłźż]+$";
}
