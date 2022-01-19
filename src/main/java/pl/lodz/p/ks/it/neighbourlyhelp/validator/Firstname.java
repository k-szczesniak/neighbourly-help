package pl.lodz.p.ks.it.neighbourlyhelp.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 3, max = 31, message = "validation.firstname.size")
@Pattern(regexp = RegularExpression.FIRSTNAME, message = "validation.firstname.pattern")
public @interface Firstname {
    String message() default "validation.firstname.pattern";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
