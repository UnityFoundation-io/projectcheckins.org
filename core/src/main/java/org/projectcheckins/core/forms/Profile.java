package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;

@Serdeable
public record Profile(@NotBlank String id,
                      @NotBlank @Email String email,
                      @NotNull Appearance appearance,
                      @NotBlank String timeZone,
                      @NotNull DayOfWeek firstDayOfWeek,
                      @NotNull TimeFormat timeFormat) {
}
