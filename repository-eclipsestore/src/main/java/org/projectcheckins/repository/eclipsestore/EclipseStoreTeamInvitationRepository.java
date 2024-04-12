package org.projectcheckins.repository.eclipsestore;

import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.eclipsestore.annotations.StoreParams;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.security.TeamInvitation;
import org.projectcheckins.security.TeamInvitationRepository;

import java.util.List;
import java.util.Optional;

@Singleton
class EclipseStoreTeamInvitationRepository implements TeamInvitationRepository {
    private final RootProvider<Data> rootProvider;

    protected EclipseStoreTeamInvitationRepository(RootProvider<Data> rootProvider) {
        this.rootProvider = rootProvider;
    }

    @Override
    public List<TeamInvitationEntity> findAll() {
        return rootProvider.root().getInvitations();
    }

    @Override
    public void save(@NotBlank @Email String email) {
        if (!canRegister(email)) {
            saveInvitation(rootProvider.root().getInvitations(), new TeamInvitationEntity(email));
        }
    }

    @Override
    public void accept(@NotBlank @Email String email) {
        findByEmail(email).ifPresent(this::saveInvitation);
    }

    private Optional<TeamInvitationEntity> findByEmail(@NotBlank @Email String email) {
        return rootProvider.root().getInvitations().stream().filter(i -> i.email().equals(email)).findAny();
    }

    @StoreParams("invitations")
    public void saveInvitation(List<TeamInvitationEntity> invitations, TeamInvitationEntity invitation) {
        invitations.add(invitation);
    }

    @StoreParams("invitation")
    public void saveInvitation(TeamInvitationEntity invitation) {
        invitation.accepted(true);
    }
}
