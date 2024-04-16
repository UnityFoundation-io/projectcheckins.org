package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.security.constraints.Unique;

@Serdeable
public record TeamMemberSave(@Unique @NonNull @NotBlank @Email String email) { }
