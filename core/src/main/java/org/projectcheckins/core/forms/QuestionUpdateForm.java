package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.List;

@Serdeable
public record QuestionUpdateForm(@NonNull String id,
                                 @NotBlank String title,
                                 @NotNull HowOften howOften,
                                 @NotNull TimeOfDay timeOfDay,
                                 @Nullable List<DayOfWeek> dailyOnDay,
                                 @Nullable DayOfWeek onceAWeekDay,
                                 @Nullable DayOfWeek everyOtherWeekDay,
                                 @Nullable DayOfWeek onceAMonthOnTheFirstDay) {
}
