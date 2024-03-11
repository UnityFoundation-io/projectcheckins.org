package org.projectcheckins.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.QuestionSave;
import java.util.TimeZone;

@MicronautTest
class QuestionSaveTest {

    @Test
    void titleIsRequired(Validator validator) {
        assertThat(validator.validate(new QuestionSave(null, "schedule", TimeZone.getDefault())))
            .isNotEmpty();
        assertThat(validator.validate(new QuestionSave("", "schedule", TimeZone.getDefault())))
            .anyMatch(x -> x.getPropertyPath().toString().equals("title") && x.getMessage().equals("must not be blank"));
        assertThat(validator.validate(new QuestionSave("What are you working on", "schedule", TimeZone.getDefault())))
            .isEmpty();
    }

    @Test
    void scheduleIsRequired(Validator validator) {
        assertThat(validator.validate(new QuestionSave("What are you working on", null, TimeZone.getDefault())))
            .isNotEmpty();
        assertThat(validator.validate(new QuestionSave("What are you working on", "", TimeZone.getDefault())))
            .anyMatch(x -> x.getPropertyPath().toString().equals("schedule") && x.getMessage().equals("must not be blank"));
    }

    @Test
    void timeZoneIsRequired(Validator validator) {
        assertThat(validator.validate(new QuestionSave("What are you working on", "schedule", null)))
                .anyMatch(x -> x.getPropertyPath().toString().equals("timeZone") && x.getMessage().equals("must not be null"));
    }

    @Test
    void questionSaveIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(QuestionSave.class)))
            .doesNotThrowAnyException();
    }

    @Test
    void questionSaveIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(QuestionSave.class)))
            .doesNotThrowAnyException();
    }
}
