package org.projectcheckins.bootstrap;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

public final class AssertUtils {
    private AssertUtils() {

    }

    public static <T> void assertValid(@NonNull Validator validator, T object) {
        assertThat(validator.validate(object))
                .isEmpty();
    }

    public static <T> void assertNotBlank(@NonNull Validator validator, T object, String propertyName) {
        assertThat(validator.validate(object))
                .anyMatch(x -> x.getPropertyPath().toString().equals(propertyName))
                .extracting(ConstraintViolation::getMessage)
                .first()
                .isEqualTo("must not be blank");
    }

    public static <T> void assertNotNull(@NonNull Validator validator, T object, String propertyName) {
        assertThat(validator.validate(object))
                .anyMatch(x -> x.getPropertyPath().toString().equals(propertyName))
                .extracting(ConstraintViolation::getMessage)
                .first()
                .isEqualTo("must not be null");
    }

    public static <T> void assertPastOrPresent(@NonNull Validator validator, T object, String propertyName) {
        assertThat(validator.validate(object))
                .anyMatch(x -> x.getPropertyPath().toString().equals(propertyName))
                .extracting(ConstraintViolation::getMessage)
                .first()
                .isEqualTo("must be a date in the past or in the present");
    }

}
