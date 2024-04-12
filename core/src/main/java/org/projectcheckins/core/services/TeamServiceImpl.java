package org.projectcheckins.core.services;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.PublicProfile;
import org.projectcheckins.core.forms.TeamMemberSave;
import org.projectcheckins.core.repositories.ProfileRepository;
import org.projectcheckins.security.TeamInvitation;
import org.projectcheckins.security.TeamInvitationRepository;

import java.util.List;

import static java.util.function.Predicate.not;

@Singleton
public class TeamServiceImpl implements TeamService {

    private final ProfileRepository profileRepository;
    private final TeamInvitationRepository teamInvitationRepository;

    public TeamServiceImpl(ProfileRepository profileRepository, TeamInvitationRepository teamInvitationRepository) {
        this.profileRepository = profileRepository;
        this.teamInvitationRepository = teamInvitationRepository;
    }

    @Override
    @NonNull
    public List<? extends PublicProfile> findAll(@Nullable Tenant tenant) {
        return profileRepository.list(tenant);
    }

    @Override
    @NonNull
    public List<? extends TeamInvitation> findPendingInvitations(@Nullable Tenant tenant) {
        return teamInvitationRepository.findAll().stream().filter(not(TeamInvitation::accepted)).toList();
    }

    @Override
    public void save(@NotNull TeamMemberSave form, @Nullable Tenant tenant) {
        teamInvitationRepository.save(form.email());
    }
}
