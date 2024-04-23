package org.projectcheckins.security;

import io.micronaut.security.authentication.Authentication;

import java.util.Optional;

@FunctionalInterface
public interface BlockingTokenValidator {
    Optional<Authentication> validateToken(String token);
}
