package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.annotations.Select;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.TimeZone;

@Serdeable
public record QuestionSave(@NotBlank String title,
                           @NotBlank String schedule,
                           @NotNull @Select(fetcher = TimeZoneFetcher.class) TimeZone timeZone) {

}
