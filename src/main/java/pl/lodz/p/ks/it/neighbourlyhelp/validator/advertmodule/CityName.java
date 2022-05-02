package pl.lodz.p.ks.it.neighbourlyhelp.validator.advertmodule;

import pl.lodz.p.ks.it.neighbourlyhelp.validator.RegularExpression;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adnotacja służąca do weryfikacji nazwy miasta.
 */
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 2, max = 31, message = "validation.city.name.size")
@Pattern(regexp = RegularExpression.CITY_NAME, message = "validation.city.name.pattern")
public @interface CityName {
    String message() default "validation.city.name.pattern";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}