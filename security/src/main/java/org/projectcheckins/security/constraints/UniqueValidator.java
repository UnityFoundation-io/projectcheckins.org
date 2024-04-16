package org.projectcheckins.security.constraints;

import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.validation.validator.constraints.ConstraintValidator;
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext;
import jakarta.inject.Singleton;
import org.projectcheckins.security.TeamInvitationRepository;
import org.projectcheckins.security.constraints.Unique;

@Singleton
class UniqueValidator implements ConstraintValidator<Unique, String> {
    private final TeamInvitationRepository teamInvitationRepository;

    UniqueValidator(TeamInvitationRepository teamInvitationRepository) {
        this.teamInvitationRepository = teamInvitationRepository;
    }

    @Override
    public boolean isValid(@Nullable String value,
                           @NonNull AnnotationValue<Unique> annotationMetadata,
                           @NonNull ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        return !teamInvitationRepository.existsByEmail(value);
    }
}
