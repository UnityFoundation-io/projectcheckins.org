package org.projectcheckins.core.forms;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest(startApplication = false)
class QuestionMapperTest {
    @Test
    void toQuestionSave(QuestionSaveMapper mapper) {
        QuestionSaveForm questionSaveForm = new QuestionSaveForm("What did you do today, and what will you work on tomorrow?");
        assertThat(mapper.toQuestionSave(questionSaveForm))
                .hasFieldOrPropertyWithValue("title", "What did you do today, and what will you work on tomorrow?")
                .hasFieldOrPropertyWithValue("howOften", HowOften.DAILY_ON)
                .hasFieldOrPropertyWithValue("timeOfDay", TimeOfDay.END)
                .hasFieldOrPropertyWithValue("days", List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));

        questionSaveForm = new QuestionSaveForm("What did you do today, and what will you work on tomorrow?", HowOften.ONCE_A_WEEK, TimeOfDay.BEGINNING, Collections.singletonList(DayOfWeek.MONDAY), DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY);
        assertThat(mapper.toQuestionSave(questionSaveForm))
                .hasFieldOrPropertyWithValue("title", "What did you do today, and what will you work on tomorrow?")
                .hasFieldOrPropertyWithValue("howOften", HowOften.ONCE_A_WEEK)
                .hasFieldOrPropertyWithValue("timeOfDay", TimeOfDay.BEGINNING)
                .hasFieldOrPropertyWithValue("days", Collections.singletonList(DayOfWeek.TUESDAY));

        questionSaveForm = new QuestionSaveForm("What did you do today, and what will you work on tomorrow?", HowOften.EVERY_OTHER_WEEK, TimeOfDay.BEGINNING, Collections.singletonList(DayOfWeek.MONDAY), DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY);
        assertThat(mapper.toQuestionSave(questionSaveForm))
                .hasFieldOrPropertyWithValue("title", "What did you do today, and what will you work on tomorrow?")
                .hasFieldOrPropertyWithValue("howOften", HowOften.EVERY_OTHER_WEEK)
                .hasFieldOrPropertyWithValue("timeOfDay", TimeOfDay.BEGINNING)
                .hasFieldOrPropertyWithValue("days", Collections.singletonList(DayOfWeek.WEDNESDAY));

        questionSaveForm = new QuestionSaveForm("What did you do today, and what will you work on tomorrow?", HowOften.ONCE_A_MONTH_ON_THE_FIRST, TimeOfDay.BEGINNING, Collections.singletonList(DayOfWeek.MONDAY), DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY);
        assertThat(mapper.toQuestionSave(questionSaveForm))
                .hasFieldOrPropertyWithValue("title", "What did you do today, and what will you work on tomorrow?")
                .hasFieldOrPropertyWithValue("howOften", HowOften.ONCE_A_MONTH_ON_THE_FIRST)
                .hasFieldOrPropertyWithValue("timeOfDay", TimeOfDay.BEGINNING)
                .hasFieldOrPropertyWithValue("days", Collections.singletonList(DayOfWeek.THURSDAY));
    }
}