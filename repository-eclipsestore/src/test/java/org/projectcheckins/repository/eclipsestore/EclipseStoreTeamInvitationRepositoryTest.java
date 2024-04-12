package org.projectcheckins.repository.eclipsestore;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@MicronautTest(startApplication = false)
class EclipseStoreTeamInvitationRepositoryTest {

    @Inject
    EclipseStoreTeamInvitationRepository teamInvitationRepository;

    @Test
    void testOperations() {
        final String email = "invitation@email.com";
        assertThat(teamInvitationRepository.findAll())
                .isEmpty();
        assertThat(teamInvitationRepository.canRegister(email))
                .isFalse();
        teamInvitationRepository.save(email);
        assertThat(teamInvitationRepository.canRegister(email))
                .isTrue();
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
