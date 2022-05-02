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
 * Adnotacja służąca do weryfikacji opisu boxu.
 */
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 1, max = 255, message = "validation.description.size")
@Pattern(regexp = RegularExpression.DESCRIPTION, message = "validation.description.pattern")
public @interface Description {
    String message() default "validation.description.pattern";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
