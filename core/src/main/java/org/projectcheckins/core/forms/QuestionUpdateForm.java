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

@NotEmptyDays
@Serdeable
public record QuestionUpdateForm(@NonNull String id,
                                 @NotBlank String title,
                                 @NotNull HowOften howOften,
                                 @NotNull TimeOfDay timeOfDay,
                                 @Nullable Set<DayOfWeek> dailyOnDay,
                                 @Nullable DayOfWeek onceAWeekDay,
                                 @Nullable DayOfWeek everyOtherWeekDay,
                                 @Nullable DayOfWeek onceAMonthOnTheFirstDay) implements QuestionUpdate, QuestionForm {
    @Override
    @NotEmpty
    public Set<DayOfWeek> days() {
        return QuestionForm.super.days();
    }
}
