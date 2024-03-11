package org.projectcheckins.core.repositories;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;

import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.Profile;
import org.projectcheckins.core.forms.ProfileUpdate;

public interface ProfileRepository {
    @NonNull
    Format findPreferedFormat(@NonNull @NotNull Authentication authentication);

    @NonNull
    Optional<Profile> findByEmail(@NonNull @NotBlank @Email String email);

    @NonNull
    Optional<Profile> findByAuthentication(@NotNull Authentication authentication, @Nullable Tenant tenant);

    void update(@NotNull Authentication authentication, @NotNull @Valid ProfileUpdate profileUpdate, @Nullable Tenant tenant);

    @NonNull
    default Optional<Profile> findByAuthentication(@NotNull Authentication authentication) {
        return findByAuthentication(authentication, null);
    }

    default void update(@NotNull Authentication authentication, @NotNull @Valid ProfileUpdate profileUpdate) {
        update(authentication, profileUpdate, null);
    }
}
