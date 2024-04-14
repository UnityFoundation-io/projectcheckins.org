package org.projectcheckins.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@FunctionalInterface
public interface UserRepository {
    boolean existsByEmail(@NotBlank @Email String email);

}
