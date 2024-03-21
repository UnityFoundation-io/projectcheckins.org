package org.projectcheckins.core.repositories;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Secondary;
import io.micronaut.context.env.Environment;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import org.projectcheckins.annotations.Generated;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.forms.ProfileUpdate;

import java.util.List;
import java.util.Optional;

@Generated // "ignore for jacoco"
@Requires(env = Environment.TEST)
@Secondary
@Singleton
public class SecondaryProfileRepository implements ProfileRepository {

    @Override
    public List<? extends Profile> list(Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Optional<? extends Profile> findByAuthentication(Authentication authentication, Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void update(Authentication authentication, ProfileUpdate profileUpdate, Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
