package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Collections;

@Singleton
class QuestionMapper implements QuestionSaveMapper, QuestionUpdateMapper {

    @Override
    @NonNull
    public QuestionUpdate toQuestionUpdate(@NonNull @NotNull @Valid QuestionUpdateForm form) {
        return new QuestionUpdate(form.id(), form.title(), form.howOften(), switch (form.howOften()) {
            case DAILY_ON -> form.dailyOnDay();
            case EVERY_OTHER_WEEK -> Collections.singletonList(form.everyOtherWeekDay());
            case ONCE_A_WEEK -> Collections.singletonList(form.onceAWeekDay());
            case ONCE_A_MONTH_ON_THE_FIRST -> Collections.singletonList(form.onceAMonthOnTheFirstDay());
        }, form.timeOfDay());
    }

    @Override
    @NonNull
    public QuestionSave toQuestionSave(@NonNull @NotNull @Valid QuestionSaveForm form) {
        return new QuestionSave(form.title(), form.howOften(), switch (form.howOften()) {
            case DAILY_ON -> form.dailyOnDay();
            case EVERY_OTHER_WEEK -> Collections.singletonList(form.everyOtherWeekDay());
            case ONCE_A_WEEK -> Collections.singletonList(form.onceAWeekDay());
            case ONCE_A_MONTH_ON_THE_FIRST -> Collections.singletonList(form.onceAMonthOnTheFirstDay());
        }, form.timeOfDay());
    }
}
