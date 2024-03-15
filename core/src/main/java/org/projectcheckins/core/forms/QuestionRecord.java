package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.projectcheckins.core.api.Question;

import java.time.DayOfWeek;
import java.util.List;

@Serdeable
public record QuestionRecord(
        @NotBlank String id,
        @NotBlank String title,
        @NotNull HowOften howOften,
        @NotEmpty Set<DayOfWeek> days,
        @NotNull TimeOfDay timeOfDay
) implements Question {
}
