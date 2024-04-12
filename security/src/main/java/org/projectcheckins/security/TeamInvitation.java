package org.projectcheckins.security;

import jakarta.validation.constraints.NotBlank;

public interface TeamInvitation {

    @NotBlank
    String email();

    boolean accepted();
}
