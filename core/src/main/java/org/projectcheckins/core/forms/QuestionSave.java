package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.DayOfWeek;
import java.util.List;

@Serdeable
public record QuestionSave(@NotBlank String title,
                           @NotNull HowOften howOften,
                           @NotEmpty Set<DayOfWeek> days,
                           @NotNull TimeOfDay timeOfDay) {

}
