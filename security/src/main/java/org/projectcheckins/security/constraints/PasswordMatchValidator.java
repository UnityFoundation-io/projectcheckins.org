package org.projectcheckins.security.constraints;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Introspected
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, RepeatPasswordForm> { @Override
    public boolean isValid(RepeatPasswordForm value, ConstraintValidatorContext context) {
        if (value.password() == null && value.repeatPassword() == null) {
            return true;
        }
        if (value.password() != null && value.repeatPassword() == null) {
            return false;
        }
        if (value.password() == null) {
            return false;
        }
        return value.password().equals(value.repeatPassword());
    }
}
