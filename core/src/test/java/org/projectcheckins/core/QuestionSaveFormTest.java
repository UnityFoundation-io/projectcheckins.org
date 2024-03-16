package org.projectcheckins.core;

import static org.projectcheckins.test.ValidationAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.HowOften;
import org.projectcheckins.core.forms.QuestionSaveForm;
import org.projectcheckins.core.forms.TimeOfDay;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.Set;

@MicronautTest
class QuestionSaveFormTest {

    @Test
    void questionSaveValidation(Validator validator) {
        assertThat(validator.validate(new QuestionSaveForm(null, null, null, null, null)))
                .hasNotBlankViolation("title")
                .hasNotNullViolation("howOften")
                .hasNotNullViolation("manyDays")
                .hasNotNullViolation("timeOfDay");
        assertThat(validator.validate(new QuestionSaveForm("", null, null, Collections.emptySet(), null)))
                .hasNotBlankViolation("title")
                .hasNotEmptyViolation("manyDays");
        assertThat(validator.validate(new QuestionSaveForm("What are you working on", HowOften.DAILY_ON, TimeOfDay.END, Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), null)))
            .isValid();
    }

    @Test
    void questionSaveFormIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(QuestionSaveForm.class)))
            .doesNotThrowAnyException();
    }

    @Test
    void questionSaveFormIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(QuestionSaveForm.class)))
            .doesNotThrowAnyException();
    }
}
