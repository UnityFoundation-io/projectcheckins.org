package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.security.constraints.Unique;

@Serdeable
@Unique
public record TenantTeamInvitation(@NotBlank @Email String email,
                                   @Nullable Tenant tenant) implements TeamInvitation {
}
