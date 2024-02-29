package io.micronaut.multitenancy;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents a tenant.
 * @author Sergio del Amo
 * @since 5.3.0
 */
public record Tenant(@NotBlank String id) {
}
