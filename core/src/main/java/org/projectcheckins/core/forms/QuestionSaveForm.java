package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.QuestionSave;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@NotEmptyDays
@Serdeable
public record QuestionSaveForm(@NotBlank String title,
                               @NotNull HowOften howOften,
                               @NotNull TimeOfDay timeOfDay,
                               @Nullable Set<DayOfWeek> dailyOnDay,
                               @Nullable DayOfWeek onceAWeekDay,
                               @Nullable DayOfWeek everyOtherWeekDay,
                               @Nullable DayOfWeek onceAMonthOnTheFirstDay,
                               @Nullable Map<String, List<Message>> fieldErrors,
                               @Nullable List<Message> errors) implements QuestionSave, QuestionForm {
    @Override
    public Set<DayOfWeek> days() {
        return QuestionForm.super.days();
    }

    public QuestionSaveForm(String title) {
        this(title, HowOften.DAILY_ON, TimeOfDay.END, Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), DayOfWeek.MONDAY, DayOfWeek.MONDAY, DayOfWeek.MONDAY, null, null);
    }
}
