package org.projectcheckins.security.constraints;

import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.validation.validator.constraints.ConstraintValidator;
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext;
import jakarta.inject.Singleton;
import org.projectcheckins.security.TeamInvitationRepository;
import org.projectcheckins.security.TenantTeamInvitation;

@Singleton
class UniqueValidator implements ConstraintValidator<Unique, TenantTeamInvitation> {
    private final TeamInvitationRepository teamInvitationRepository;

    UniqueValidator(TeamInvitationRepository teamInvitationRepository) {
        this.teamInvitationRepository = teamInvitationRepository;
    }

    @Override
    public boolean isValid(@Nullable TenantTeamInvitation invitation,
                           @NonNull AnnotationValue<Unique> annotationMetadata,
                           @NonNull ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(invitation.email())) {
            return true;
        }
        return !teamInvitationRepository.existsByEmail(invitation.email(), invitation.tenant());
    }
}
