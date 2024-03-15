package org.projectcheckins.core;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.HowOften;
import org.projectcheckins.core.forms.QuestionUpdate;
import org.projectcheckins.core.forms.TimeOfDay;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;

import static org.projectcheckins.test.ValidationAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@MicronautTest(startApplication = false)
class QuestionUpdateTest {
    @Test
    void questionUpdateValidation(Validator validator) {
        assertThat(validator.validate(new QuestionUpdate(null, null, null, null, null)))
                .hasNotBlankViolation("id")
                .hasNotBlankViolation("title")
                .hasNotNullViolation("howOften")
                .hasNotNullViolation("days")
                .hasNotNullViolation("timeOfDay");
        assertThat(validator.validate(new QuestionUpdate("", "", null, Collections.emptyList(), null)))
                .hasNotBlankViolation("id")
                .hasNotBlankViolation("title")
                .hasSizeViolation("days", 1, 7);
        assertThat(validator.validate(new QuestionUpdate("xxx", "What are you working on", HowOften.DAILY_ON, List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), TimeOfDay.END)))
                .isValid();
    }

    @Test
    void questionUpdateIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(QuestionUpdate.class)))
            .doesNotThrowAnyException();
    }

    @Test
    void questionUpdateIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(QuestionUpdate.class)))
            .doesNotThrowAnyException();
    }
}
