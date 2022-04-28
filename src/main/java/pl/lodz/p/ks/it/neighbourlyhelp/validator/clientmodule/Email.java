package pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule;

import pl.lodz.p.ks.it.neighbourlyhelp.validator.RegularExpression;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = RegularExpression.EMAIL, message = "validation.email.pattern")
public @interface Email {
    String message() default "validation.email.pattern";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
