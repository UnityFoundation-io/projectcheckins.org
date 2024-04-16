package org.projectcheckins.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface TeamInvitation {
    @Nullable
    Tenant tenant();

    @Email
    @NotBlank
    String email();
}
