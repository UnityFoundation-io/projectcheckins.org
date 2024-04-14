package org.projectcheckins.security;

import io.micronaut.views.fields.messages.Message;
import jakarta.inject.Singleton;
import java.util.Optional;

@Singleton
public class UserNotInvitedRegistrationCheck implements RegistrationCheck {
    private static final Message MESSAGE_USER_NOT_INVITED = new Message("User not invited", "user.not.invited");
    private static final RegistrationCheckViolation VIOLATION_USER_NOT_INVITED = new RegistrationCheckViolation(MESSAGE_USER_NOT_INVITED);
    private final TeamInvitationRepository teamInvitationRepository;

    public UserNotInvitedRegistrationCheck(TeamInvitationRepository teamInvitationRepository) {
        this.teamInvitationRepository = teamInvitationRepository;
    }

    @Override
    public Optional<RegistrationCheckViolation> validate(String email) {
        return teamInvitationRepository.existsByEmail(email)
                ? Optional.empty()
                : Optional.of(VIOLATION_USER_NOT_INVITED);
    }
}
