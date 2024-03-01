package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;

public interface UserRepository {

    void enable(@NonNull @NotBlank String userId);
}
