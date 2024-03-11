package org.projectcheckins.core.repositories;

import io.micronaut.context.annotation.Secondary;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.Profile;
import org.projectcheckins.core.forms.ProfileUpdate;

import java.util.Optional;

@Secondary
@Singleton
public class SecondaryProfileRepository implements ProfileRepository {
    @Override
    @NonNull
    public Format findPreferedFormat(@NonNull @NotNull Authentication authentication) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Optional<Profile> findByEmail(String email) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Optional<Profile> findByAuthentication(Authentication authentication, Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void update(@NotNull Authentication authentication, @NotNull @Valid ProfileUpdate profileUpdate, @Nullable Tenant tenant) {

    }
}
