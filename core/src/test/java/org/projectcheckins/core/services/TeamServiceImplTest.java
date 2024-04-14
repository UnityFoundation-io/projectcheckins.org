package org.projectcheckins.core.services;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.ProfileRecord;
import org.projectcheckins.core.forms.TeamMemberSave;
import org.projectcheckins.core.forms.TimeFormat;
import org.projectcheckins.core.repositories.ProfileRepository;
import org.projectcheckins.core.repositories.SecondaryProfileRepository;
import org.projectcheckins.security.SecondaryTeamInvitationRepository;
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

    static final TeamInvitation INVITATION_1 = new TeamInvitationRecord("pending1@email.com");

    static final TeamInvitation INVITATION_2 = new TeamInvitationRecord("pending2@email.com");

    @Test
    void testFindPendingInvitations() {
        assertThat(teamService.findInvitations(null))
                .isEqualTo(List.of(INVITATION_1, INVITATION_2));
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
    @Replaces(TeamInvitationRepository.class)
    static class TeamInvitationRepositoryMock extends SecondaryTeamInvitationRepository {
        @Override
        public List<? extends TeamInvitation> findAll() {
            return List.of(INVITATION_1, INVITATION_2);
        }

        @Override
        public void save(@NotBlank @Email String email) {
        }
    }

    record TeamInvitationRecord(String email) implements TeamInvitation { }
}
