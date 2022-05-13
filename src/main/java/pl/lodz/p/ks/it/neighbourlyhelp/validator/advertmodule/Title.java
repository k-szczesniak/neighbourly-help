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
 * Adnotacja służąca do weryfikacji tytułu ogłoszenia.
 */
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 2, max = 31, message = "validation.title.size")
@Pattern(regexp = RegularExpression.TITLE, message = "validation.title.pattern")
public @interface Title {
    String message() default "validation.title.pattern";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}