package org.projectcheckins.core.forms;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.projectcheckins.core.AssertUtils.*;

@MicronautTest(startApplication = false)
class AnswerUpdateTest {

    @Test
    void answerUpdateTextIsRequired(Validator validator) {
        assertNotBlank(validator, new AnswerUpdate("yyy", "xxx", Format.MARKDOWN, LocalDate.now(), null), "text");
        assertNotBlank(validator, new AnswerUpdate("yyy", "xxx", Format.MARKDOWN, LocalDate.now(), null), "text");
    }

    @Test
    void answerUpdateValidAnswerUpdate(Validator validator) {
        assertValid(validator, new AnswerUpdate("yyy", "xxx", Format.MARKDOWN, LocalDate.now(), "I worked on bla bla bla"));
    }

    @Test
    void answerUpdateQuestionIdIsRequired(Validator validator) {
        assertNotBlank(validator, new AnswerUpdate(null, "xxx", Format.MARKDOWN, LocalDate.now(),  "I worked on bla bla bla"), "questionId");
        assertNotBlank(validator, new AnswerUpdate("", "xxx", Format.MARKDOWN, LocalDate.now(),  "I worked on bla bla bla"), "questionId");
    }

    @Test
    void answerUpdateIdIsRequired(Validator validator) {
        assertNotBlank(validator, new AnswerUpdate("yyy", null, Format.MARKDOWN, LocalDate.now(),  "I worked on bla bla bla"), "id");
        assertNotBlank(validator, new AnswerUpdate("yyy", "", Format.MARKDOWN, LocalDate.now(),  "I worked on bla bla bla"), "id");
    }

    @Test
    void answerUpdateDateIsRequired(Validator validator) {
        assertNotNull(validator, new AnswerUpdate("yyy", "xxx", Format.MARKDOWN, null,"I worked on bla bla bla"), "answerDate");
    }

    @Test
    void answerUpdateDateIsPresentOrPast(Validator validator) {
        LocalDate answerDate = LocalDate.now().plusDays(2);
        assertPastOrPresent(validator, new AnswerUpdate("yyyy", "xxx", Format.MARKDOWN, answerDate, "I worked on bla bla bla"), "answerDate");
    }

    @Test
    void answerUpdateIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(AnswerUpdate.class)))
                .doesNotThrowAnyException();
    }

    @Test
    void answerUpdateIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(AnswerUpdate.class)))
                .doesNotThrowAnyException();
    }

}