package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.TimeZone;

@Serdeable
public record ProfileSave(@NotBlank @Email String email,
                          @NotNull Appearance appearance,
                          @NotNull TimeZone timeZone,
                          @NotNull DayOfWeek firstDayOfWeek,
                          @NotNull TimeFormat timeFormat) {
}
