package pl.lodz.p.ks.it.neighbourlyhelp.validator;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Klasa przeprowadzająca walidację typu wyliczeniowego
 */
public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {
    private List<String> acceptedValues;

    private static final String SPLIT_CHAR = ",";

    @Override
    public void initialize(ValueOfEnum constraintAnnotation) {
        acceptedValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());

        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return Arrays.stream(StringUtils.defaultString(value.toString()).split(SPLIT_CHAR))
                .filter(StringUtils::isNotEmpty)
                .allMatch(x -> acceptedValues.contains(x));
    }
}