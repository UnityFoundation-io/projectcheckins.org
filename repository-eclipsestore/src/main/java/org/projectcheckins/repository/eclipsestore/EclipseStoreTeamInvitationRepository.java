package org.projectcheckins.repository.eclipsestore;

import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.eclipsestore.annotations.StoreParams;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.security.TeamInvitationRepository;
import org.projectcheckins.security.constraints.Unique;

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
    public void save(@Unique @NotBlank @Email String email) {
        if (findByEmail(email).isEmpty()) {
            saveInvitation(rootProvider.root().getInvitations(), new TeamInvitationEntity(email));
        }
    }

    @Override
    public void deleteByEmail(@NotBlank @Email String email) {
        findByEmail(email).ifPresent(e -> delete(rootProvider.root().getInvitations(), e));
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    private Optional<TeamInvitationEntity> findByEmail(@NotBlank @Email String email) {
        return rootProvider.root().getInvitations().stream().filter(i -> i.email().equals(email)).findAny();
    }

    @StoreParams("invitations")
    public void saveInvitation(List<TeamInvitationEntity> invitations, TeamInvitationEntity invitation) {
        invitations.add(invitation);
    }

    @StoreParams("invitations")
    public void delete(List<TeamInvitationEntity> invitations, TeamInvitationEntity invitation) {
        invitations.remove(invitation);
    }
}
