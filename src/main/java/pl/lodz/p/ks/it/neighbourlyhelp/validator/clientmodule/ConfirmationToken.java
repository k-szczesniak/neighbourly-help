package pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule;

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
 * Adnotacja służąca do weryfikacji kodu weryfikacjnego.
 */
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 1, max = 128, message = "validation.pencode.size")
@Pattern(regexp = RegularExpression.CONFIRMATION_TOKEN, message = "validation.pencode.pattern")
public @interface ConfirmationToken {
    String message() default "validation.pencode.size";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}