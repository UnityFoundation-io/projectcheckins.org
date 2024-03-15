package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.annotations.InputHidden;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.DayOfWeek;
import java.util.List;


import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.DayOfWeek;
import java.util.List;

@Serdeable
public record QuestionUpdate(@NotBlank String id,
                             @NotBlank String title,
                             @NotNull HowOften howOften,
                             @NotNull @Size(min = 1, max = 7) List<DayOfWeek> days,
                             @NotNull TimeOfDay timeOfDay) {
}
