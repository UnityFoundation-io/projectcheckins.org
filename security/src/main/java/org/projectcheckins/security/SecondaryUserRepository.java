package org.projectcheckins.security;

import io.micronaut.context.annotation.Secondary;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Secondary
@Singleton
public class SecondaryUserRepository implements UserRepository {
    @Override
    public boolean existsByEmail(@NotBlank @Email String email) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
