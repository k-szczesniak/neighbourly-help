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
@Pattern(regexp = RegularExpression.ROLE, message = "validation.role.pattern")
public @interface Role {
    String message() default "validation.role.pattern";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}