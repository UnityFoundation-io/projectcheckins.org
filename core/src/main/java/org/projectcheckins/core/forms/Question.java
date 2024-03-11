package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.TimeZone;

@Serdeable
public record Question(@NotBlank String id,
                       @NotBlank String title,
                       @NotBlank String schedule,
                       @NotNull TimeZone timeZone,
                       @Nullable ZonedDateTime nextExecution) {
}
