package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;
import java.util.Set;

import static java.util.function.Predicate.not;

@Introspected
class NotEmptyRespondentsValidator implements ConstraintValidator<NotEmptyRespondents, QuestionForm> {
    @Override
    public boolean isValid(QuestionForm value, ConstraintValidatorContext context) {
        return Optional.ofNullable(value.respondentIds()).filter(not(Set::isEmpty)).isPresent();
    }
}
