package pl.lodz.p.ks.it.neighbourlyhelp.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adnotacja służąca do weryfikacji typów wyliczeniowych
 */
@Constraint(validatedBy = {ValueOfEnumValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueOfEnum {

    Class<? extends Enum<?>> enumClass();

    String message() default "validation.error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}