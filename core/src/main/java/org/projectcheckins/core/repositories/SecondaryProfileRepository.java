package org.projectcheckins.core.repositories;

import io.micronaut.context.annotation.Secondary;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.forms.ProfileUpdate;

import java.util.Optional;

@Secondary
@Singleton
public class SecondaryProfileRepository implements ProfileRepository {
    @Override
    public Optional<? extends Profile> findByAuthentication(Authentication authentication, Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void update(Authentication authentication, ProfileUpdate profileUpdate, Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
