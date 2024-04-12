package org.projectcheckins.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface RegistrationCheck {

    boolean canRegister(@NotBlank @Email String email);
}
