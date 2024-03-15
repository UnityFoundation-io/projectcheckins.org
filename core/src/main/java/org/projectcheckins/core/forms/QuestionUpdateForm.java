package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.QuestionUpdate;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.Set;

@Serdeable
public record QuestionUpdateForm(@NonNull String id,
                                 @NotBlank String title,
                                 @NotNull HowOften howOften,
                                 @NotNull TimeOfDay timeOfDay,
                                 @NotEmpty Set<DayOfWeek> dailyOnDay,
                                 @Nullable DayOfWeek onceAWeekDay,
                                 @Nullable DayOfWeek everyOtherWeekDay,
                                 @Nullable DayOfWeek onceAMonthOnTheFirstDay) implements QuestionUpdate {
    @Override
    public Set<DayOfWeek> days() {
        return switch (howOften) {
            case DAILY_ON -> dailyOnDay;
            case EVERY_OTHER_WEEK -> Collections.singleton(everyOtherWeekDay);
            case ONCE_A_WEEK -> Collections.singleton(onceAWeekDay);
            case ONCE_A_MONTH_ON_THE_FIRST -> Collections.singleton(onceAMonthOnTheFirstDay);
        };
    }
}
