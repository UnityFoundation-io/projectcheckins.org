package org.projectcheckins.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Question;
import java.util.TimeZone;

@MicronautTest
class QuestionTest {

    @Test
    void idCannotBeBlank(Validator validator) {
        assertThat(validator.validate(new Question(null, "What are you working on", "schedule", TimeZone.getDefault(), null)))
            .isNotEmpty();
        assertThat(validator.validate(new Question("", "What are you working on", "schedule", TimeZone.getDefault(), null)))
            .anyMatch(x -> x.getPropertyPath().toString().equals("id") && x.getMessage().equals("must not be blank"));
        assertThat(validator.validate(new Question("xxx", "What are you working on", "schedule", TimeZone.getDefault(), null)))
            .isEmpty();
    }

    @Test
    void titleCannotBeBlank(Validator validator) {
        assertThat(validator.validate(new Question("xxx", null, "schedule", TimeZone.getDefault(), null)))
            .isNotEmpty();
        assertThat(validator.validate(new Question("xxx", "", "schedule", TimeZone.getDefault(), null)))
            .anyMatch(x -> x.getPropertyPath().toString().equals("title") && x.getMessage().equals("must not be blank"));
        assertThat(validator.validate(new Question("xxx", "What are you working on", "schedule", TimeZone.getDefault(), null)))
            .isEmpty();
    }

    @Test
    void scheduleCannotBeBlank(Validator validator) {
        assertThat(validator.validate(new Question("xxx", "What are you working on", null, TimeZone.getDefault(), null)))
                .isNotEmpty();
        assertThat(validator.validate(new Question("xxx", "What are you working on", "", TimeZone.getDefault(), null)))
                .anyMatch(x -> x.getPropertyPath().toString().equals("schedule") && x.getMessage().equals("must not be blank"));
    }

    @Test
    void timeZoneCannotBeNull(Validator validator) {
        assertThat(validator.validate(new Question("xxx", "What are you working on", "schedule", null, null)))
                .anyMatch(x -> x.getPropertyPath().toString().equals("timeZone") && x.getMessage().equals("must not be null"));
    }

    @Test
    void questionSaveIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(Question.class)))
            .doesNotThrowAnyException();
    }

    @Test
    void questionSaveIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(Question.class)))
            .doesNotThrowAnyException();
    }
}
