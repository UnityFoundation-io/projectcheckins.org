package org.projectcheckins.core.services;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.ProfileRecord;
import org.projectcheckins.core.forms.TeamMemberSave;
import org.projectcheckins.core.forms.TimeFormat;
import org.projectcheckins.core.repositories.ProfileRepository;
import org.projectcheckins.core.repositories.SecondaryProfileRepository;
import org.projectcheckins.security.TeamInvitation;
import org.projectcheckins.security.TeamInvitationRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.TimeZone;

import static java.time.DayOfWeek.MONDAY;
import static org.assertj.core.api.Assertions.*;

@Property(name = "spec.name", value = "TeamServiceImplTest")
@MicronautTest(startApplication = false)
class TeamServiceImplTest {

    static final Profile USER_1 = new ProfileRecord(
            "user1",
            "user1@email.com",
            TimeZone.getDefault(),
            MONDAY,
            LocalTime.of(9, 0),
            LocalTime.of(16, 30),
            TimeFormat.TWENTY_FOUR_HOUR_CLOCK,
            Format.MARKDOWN,
            null,
            null
    );

    static final TeamInvitation INVITATION_1 = new TeamInvitationRecord("pending1@email.com", false);

    static final TeamInvitation INVITATION_2 = new TeamInvitationRecord("pending2@email.com", true);

    @Test
    void testFindPendingInvitations() {
        assertThat(teamService.findPendingInvitations(null))
                .isEqualTo(List.of(INVITATION_1));
    }

    @Inject
    TeamServiceImpl teamService;

    @Test
    void testFindAll() {
        assertThat(teamService.findAll(null))
                .isEqualTo(List.of(USER_1));
    }

    @Test
    void testSave() {
        final TeamMemberSave form = new TeamMemberSave("user2@email.com");
        assertThatCode(() -> teamService.save(form, null))
                .doesNotThrowAnyException();
    }

    @Test
    void testSaveInvalidEmail() {
        final TeamMemberSave form = new TeamMemberSave("not an email");
        assertThatThrownBy(() -> teamService.save(form, null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("save.form.email: must be a well-formed email address");
    }

    @Requires(property = "spec.name", value = "TeamServiceImplTest")
    @Singleton
    @Replaces(ProfileRepository.class)
    static class ProfileRepositoryMock extends SecondaryProfileRepository {
        @Override
        public List<? extends Profile> list(Tenant tenant) {
            return List.of(USER_1);
        }
    }

    @Requires(property = "spec.name", value = "TeamServiceImplTest")
    @Singleton
    static class TeamInvitationRepositoryMock implements TeamInvitationRepository {
        @Override
        public List<? extends TeamInvitation> findAll() {
            return List.of(INVITATION_1, INVITATION_2);
        }

        @Override
        public void save(String email) {
        }

        @Override
        public void accept(String email) {

        }
    }

    record TeamInvitationRecord(String email, boolean accepted) implements TeamInvitation { }
}
