package org.projectcheckins.core.forms;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.projectcheckins.test.AssertUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@MicronautTest
class AnswerSaveTest {
    @Test
    void textIsRequired(Validator validator) {
        assertNotBlank(validator, new AnswerSave("xxx", Format.MARKDOWN, LocalDate.now(), null), "text");
    }

    @Test
    void validAnswerSave(Validator validator) {
        assertThat(validator.validate(new AnswerSave("xxx", Format.MARKDOWN, LocalDate.now(), "I worked on bla bla bla")))
            .isEmpty();
    }

    @Test
    void questionIdIsRequired(Validator validator) {
        assertNotBlank(validator, new AnswerSave(null, Format.MARKDOWN, LocalDate.now(),  "I worked on bla bla bla"), "questionId");
        assertNotBlank(validator, new AnswerSave("", Format.MARKDOWN, LocalDate.now(),  "I worked on bla bla bla"), "questionId");
    }

    @Test
    void answerDateIsRequired(Validator validator) {
        assertNotNull(validator, new AnswerSave("xxx", Format.MARKDOWN,  null,"I worked on bla bla bla"), "answerDate");
    }

    @Test
    void answerDateIsPresentOrPast(Validator validator) {
        LocalDate answerDate = LocalDate.now().plusDays(2);
        assertPastOrPresent(validator, new AnswerSave("xxx", Format.MARKDOWN, answerDate, "I worked on bla bla bla"), "answerDate");
    }

    @Test
    void answerSaveIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(AnswerSave.class)))
            .doesNotThrowAnyException();
    }

    @Test
    void answerSaveIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(AnswerSave.class)))
            .doesNotThrowAnyException();
    }
}
