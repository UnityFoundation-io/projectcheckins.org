package org.projectcheckins.test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.Set;

public class ValidationAssert extends AbstractAssert<ValidationAssert, Object> {

    private final Validator validator;

    public ValidationAssert(Validator validator, Object actual) {
        super(actual, ValidationAssert.class);
        this.validator = validator;
    }

    public static ValidationAssert assertThat(Validator validator, Object actual) {
        return new ValidationAssert(validator, actual);
    }

    public ValidationAssert fieldNotBlank(String name) {
        expectedViolationMessage(name, "must not be blank");
        return this;
    }

    public ValidationAssert isValid() {
        Set<ConstraintViolation<Object>> violations = validator.validate(actual);
        Assertions.assertThat(violations).isEmpty();
        return this;
    }

    public ValidationAssert fieldNotNull(String name) {
        expectedViolationMessage(name, "must not be null");
        return this;
    }

    private void expectedViolationMessage(String name, String message) {
        Set<ConstraintViolation<Object>> violations = validator.validate(actual);
        Assertions.assertThat(violations)
                .anyMatch(x -> x.getPropertyPath().toString().equals(name))
                .extracting(ConstraintViolation::getMessage)
                .first()
                .isEqualTo(message);
    }
}
