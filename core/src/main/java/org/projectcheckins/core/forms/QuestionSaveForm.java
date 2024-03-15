package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.List;

@Serdeable
public record QuestionSaveForm(@NotBlank String title,
                               @NotNull HowOften howOften,
                               @NotNull TimeOfDay timeOfDay,
                               @Nullable List<DayOfWeek> dailyOnDay,
                               @Nullable DayOfWeek onceAWeekDay,
                               @Nullable DayOfWeek everyOtherWeekDay,
                               @Nullable DayOfWeek onceAMonthOnTheFirstDay) {

    public QuestionSaveForm(String title) {
        this(title, HowOften.DAILY_ON, TimeOfDay.END, List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), DayOfWeek.MONDAY, DayOfWeek.MONDAY, DayOfWeek.MONDAY);
    }
}
