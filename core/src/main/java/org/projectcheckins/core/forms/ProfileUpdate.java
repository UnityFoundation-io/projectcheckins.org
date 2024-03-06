package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.annotations.InputHidden;
import io.micronaut.views.fields.annotations.Select;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;

@Serdeable
public record ProfileUpdate(@NotBlank @InputHidden String id,
                            @NotNull @Select(fetcher = TimeZoneFetcher.class) String timeZone,
                            @NotNull DayOfWeek firstDayOfWeek,
                            @NotNull TimeFormat timeFormat) {
}
