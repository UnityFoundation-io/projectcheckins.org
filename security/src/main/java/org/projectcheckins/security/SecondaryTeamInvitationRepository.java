package org.projectcheckins.security;

import io.micronaut.context.annotation.Secondary;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Secondary
@Singleton
public class SecondaryTeamInvitationRepository implements TeamInvitationRepository {
    @Override
    public List<? extends TeamInvitation> findAll() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void save(@NotBlank @Email String email) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteByEmail(@NotBlank @Email String email) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean existsByEmail(@NotBlank @Email String email) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
