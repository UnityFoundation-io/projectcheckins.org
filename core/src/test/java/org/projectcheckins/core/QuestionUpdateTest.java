package org.projectcheckins.core;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.QuestionUpdate;
import java.util.TimeZone;

import static org.projectcheckins.test.ValidationAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@MicronautTest
class QuestionUpdateTest {

    @Test
    void idCannotBeBlank(Validator validator) {
        assertThat(validator.validate(new QuestionUpdate(null, "What are you working on", "schedule", TimeZone.getDefault())))
            .fieldNotBlank("id");
        assertThat(validator.validate(new QuestionUpdate("", "What are you working on", "schedule", TimeZone.getDefault())))
            .fieldNotBlank("id");
        assertThat(validator.validate(new QuestionUpdate("xxx", "What are you working on", "schedule", TimeZone.getDefault())))
            .isValid();
    }

    @Test
    void titleCannotBeBlank(Validator validator) {
        assertThat(validator.validate(new QuestionUpdate("xxx", null, "schedule", TimeZone.getDefault())))
            .fieldNotBlank("title");
        assertThat(validator.validate(new QuestionUpdate("xxx", "", "schedule", TimeZone.getDefault())))
            .fieldNotBlank("title");
    }

    @Test
    void scheduleCannotBeBlank(Validator validator) {
        assertThat(validator.validate(new QuestionUpdate("xxx", "What are you working on", null, TimeZone.getDefault())))
            .fieldNotBlank("schedule");
        assertThat(validator.validate(new QuestionUpdate("xxx", "What are you working on", "", TimeZone.getDefault())))
            .fieldNotBlank("schedule");
    }
    @Test
    void timeZoneCannotBeNull(Validator validator) {
        assertThat(validator.validate(new QuestionUpdate("xxx", "What are you working on", "schedule", null)))
            .hasNotNullViolation("timeZone");
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
