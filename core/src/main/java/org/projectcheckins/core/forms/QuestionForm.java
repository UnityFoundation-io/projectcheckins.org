package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.Set;

public interface QuestionForm extends Fieldset {
    @NotNull HowOften howOften();
    @Nullable
    Set<DayOfWeek> dailyOnDay();
    @Nullable
    DayOfWeek onceAWeekDay();
    @Nullable
    DayOfWeek everyOtherWeekDay();
    @Nullable
    DayOfWeek onceAMonthOnTheFirstDay();

    default Set<DayOfWeek> days() {
        if (howOften() == null) {
            return Collections.emptySet();
        }
        return switch (howOften()) {
            case DAILY_ON -> dailyOnDay() != null ? dailyOnDay() : Collections.emptySet();
            case EVERY_OTHER_WEEK -> everyOtherWeekDay() != null ? Collections.singleton(everyOtherWeekDay()) : Collections.emptySet();
            case ONCE_A_WEEK -> onceAWeekDay() != null ? Collections.singleton(onceAWeekDay()) : Collections.emptySet();
            case ONCE_A_MONTH_ON_THE_FIRST -> onceAMonthOnTheFirstDay() != null ? Collections.singleton(onceAMonthOnTheFirstDay()) : Collections.emptySet();
        };
    }
}
