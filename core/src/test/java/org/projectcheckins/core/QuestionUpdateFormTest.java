package org.projectcheckins.core;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.HowOften;
import org.projectcheckins.core.forms.TimeOfDay;
import org.projectcheckins.core.forms.QuestionUpdateForm;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.Set;

import static org.projectcheckins.test.ValidationAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@MicronautTest(startApplication = false)
class QuestionUpdateFormTest {
    @Test
    void questionUpdateValidation(Validator validator) {
        assertThat(validator.validate(new QuestionUpdateForm(null, null, null, null, null, null, null, null)))
                .hasNotBlankViolation("id")
                .hasNotBlankViolation("title")
                .hasNotNullViolation("howOften")
                .hasNotNullViolation("dailyOnDay")
                .hasNotNullViolation("timeOfDay");
        assertThat(validator.validate(new QuestionUpdateForm("", "", null, null, Collections.emptySet(), null, null, null)))
                .hasNotBlankViolation("id")
                .hasNotBlankViolation("title")
                .hasNotEmptyViolation("dailyOnDay");
        assertThat(validator.validate(new QuestionUpdateForm("xxx", "What are you working on", HowOften.DAILY_ON, TimeOfDay.END, Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), null, null, null)))
                .isValid();
    }

    @Test
    void questionUpdateFormIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(QuestionUpdateForm.class)))
            .doesNotThrowAnyException();
    }

    @Test
    void questionUpdateFormIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(QuestionUpdateForm.class)))
            .doesNotThrowAnyException();
    }
}
