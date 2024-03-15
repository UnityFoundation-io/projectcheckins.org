package org.projectcheckins.core.forms;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest(startApplication = false)
class QuestionUpdateMapperTest {
    @Test
    void toQuestionUpdate(QuestionUpdateMapper mapper) {
        QuestionUpdateForm form = new QuestionUpdateForm("xxx", "What did you do today, and what will you work on tomorrow?", HowOften.DAILY_ON, TimeOfDay.END, Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), DayOfWeek.MONDAY, DayOfWeek.MONDAY, DayOfWeek.MONDAY);
        assertThat(mapper.toQuestionUpdate(form))
                .hasFieldOrPropertyWithValue("title", "What did you do today, and what will you work on tomorrow?")
                .hasFieldOrPropertyWithValue("howOften", HowOften.DAILY_ON)
                .hasFieldOrPropertyWithValue("timeOfDay", TimeOfDay.END)
                .hasFieldOrPropertyWithValue("days", Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));

        form = new QuestionUpdateForm("xxx", "What did you do today, and what will you work on tomorrow?", HowOften.ONCE_A_WEEK, TimeOfDay.BEGINNING, Collections.singleton(DayOfWeek.MONDAY), DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY);
        assertThat(mapper.toQuestionUpdate(form))
                .hasFieldOrPropertyWithValue("title", "What did you do today, and what will you work on tomorrow?")
                .hasFieldOrPropertyWithValue("howOften", HowOften.ONCE_A_WEEK)
                .hasFieldOrPropertyWithValue("timeOfDay", TimeOfDay.BEGINNING)
                .hasFieldOrPropertyWithValue("days", Collections.singleton(DayOfWeek.TUESDAY));

        form = new QuestionUpdateForm("xxx", "What did you do today, and what will you work on tomorrow?", HowOften.EVERY_OTHER_WEEK, TimeOfDay.BEGINNING, Collections.singleton(DayOfWeek.MONDAY), DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY);
        assertThat(mapper.toQuestionUpdate(form))
                .hasFieldOrPropertyWithValue("title", "What did you do today, and what will you work on tomorrow?")
                .hasFieldOrPropertyWithValue("howOften", HowOften.EVERY_OTHER_WEEK)
                .hasFieldOrPropertyWithValue("timeOfDay", TimeOfDay.BEGINNING)
                .hasFieldOrPropertyWithValue("days", Collections.singleton(DayOfWeek.WEDNESDAY));

        form = new QuestionUpdateForm("xxx", "What did you do today, and what will you work on tomorrow?", HowOften.ONCE_A_MONTH_ON_THE_FIRST, TimeOfDay.BEGINNING, Collections.singleton(DayOfWeek.MONDAY), DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY);
        assertThat(mapper.toQuestionUpdate(form))
                .hasFieldOrPropertyWithValue("title", "What did you do today, and what will you work on tomorrow?")
                .hasFieldOrPropertyWithValue("howOften", HowOften.ONCE_A_MONTH_ON_THE_FIRST)
                .hasFieldOrPropertyWithValue("timeOfDay", TimeOfDay.BEGINNING)
                .hasFieldOrPropertyWithValue("days", Collections.singleton(DayOfWeek.THURSDAY));
    }
}
