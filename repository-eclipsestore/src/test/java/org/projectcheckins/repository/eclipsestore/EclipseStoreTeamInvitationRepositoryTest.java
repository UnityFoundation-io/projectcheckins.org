package org.projectcheckins.repository.eclipsestore;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.*;

import static org.assertj.core.api.Assertions.*;

@MicronautTest(startApplication = false)
class EclipseStoreTeamInvitationRepositoryTest {

    @Inject
    TeamInvitationRepository teamInvitationRepository;

    @Test
    void testOperations() {
        final String email = "invitation@email.com";
        assertThat(teamInvitationRepository.findAll())
                .isEmpty();
        teamInvitationRepository.save(email);
        assertThat(teamInvitationRepository.findAll())
                .hasSize(1);
        assertThat(teamInvitationRepository.findAll().get(0))
                .hasFieldOrPropertyWithValue("accepted", false);
        assertThatCode(() -> teamInvitationRepository.save(email))
                .doesNotThrowAnyException();
        teamInvitationRepository.accept(email);
        assertThat(teamInvitationRepository.findAll().get(0))
                .hasFieldOrPropertyWithValue("accepted", true);
    }
}
