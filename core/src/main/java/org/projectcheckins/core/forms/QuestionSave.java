package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.Set;

@Serdeable
public record QuestionSave(@NotBlank String title,
                           @NotNull HowOften howOften,
                           @NotEmpty Set<DayOfWeek> days,
                           @NotNull TimeOfDay timeOfDay) {

}
