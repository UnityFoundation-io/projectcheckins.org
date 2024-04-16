package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface TeamInvitationRepository {

    @NonNull
    List<? extends TeamInvitation> findAll(@Nullable Tenant tenant);

    void save(@NonNull @NotNull @Valid TenantTeamInvitation invitation);

    default void save(@NonNull @NotBlank @Email String email) {
        save(new TenantTeamInvitation(email, null));
    }

    void deleteByEmail(@NotBlank @Email String email, @Nullable Tenant tenant);

    boolean existsByEmail(@NotBlank @Email String email, @Nullable Tenant tenant);
}
