package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

import java.time.DayOfWeek;
import java.util.List;
import java.util.TimeZone;

@Serdeable
public record UserSave(@NonNull @NotBlank String email,
                       @NonNull @NotBlank String encodedPassword,
                       @NonNull List<String> authorities) {
}
