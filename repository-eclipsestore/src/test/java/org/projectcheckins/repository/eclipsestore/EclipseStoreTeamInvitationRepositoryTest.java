package org.projectcheckins.repository.eclipsestore;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.UserNotInvitedRegistrationCheck;

import static org.assertj.core.api.Assertions.*;

@MicronautTest(startApplication = false)
class EclipseStoreTeamInvitationRepositoryTest {
    @Test
    void testOperations(EclipseStoreTeamInvitationRepository teamInvitationRepository,
                        UserNotInvitedRegistrationCheck userNotInvitedRegistrationCheck) {
        final String email = "invitation@email.com";
        assertThat(teamInvitationRepository.findAll())
                .isEmpty();
        assertThat(userNotInvitedRegistrationCheck.validate(email))
                .isNotEmpty();
        teamInvitationRepository.save(email);
        assertThat(userNotInvitedRegistrationCheck.validate(email))
                .isEmpty();
        assertThat(teamInvitationRepository.findAll())
                .hasSize(1);
        assertThat(teamInvitationRepository.findAll().get(0))
                .hasFieldOrPropertyWithValue("email", email);
        assertThatCode(() -> teamInvitationRepository.save(email))
                .doesNotThrowAnyException();
    }
}
