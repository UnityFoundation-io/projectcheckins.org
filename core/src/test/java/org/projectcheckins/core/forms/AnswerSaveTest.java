package org.projectcheckins.core.forms;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@MicronautTest
class AnswerSaveTest {
    @Test
    void textIsRequired(Validator validator) {
        assertThat(validator.validate(new AnswerSave("xxx", LocalDate.now(),  null)))
            .isNotEmpty();
        assertThat(validator.validate(new AnswerSave("xxx", LocalDate.now(),"")))
            .anyMatch(x -> x.getPropertyPath().toString().equals("text") && x.getMessage().equals("must not be blank"));
        assertThat(validator.validate(new AnswerSave("xxx", LocalDate.now(), "I worked on bla bla bla")))
            .isEmpty();
    }

    @Test
    void questionIdIsRequired(Validator validator) {
        assertThat(validator.validate(new AnswerSave(null, LocalDate.now(),  "I worked on bla bla bla")))
                .isNotEmpty();
        assertThat(validator.validate(new AnswerSave("", LocalDate.now(),"I worked on bla bla bla")))
                .anyMatch(x -> x.getPropertyPath().toString().equals("questionId") && x.getMessage().equals("must not be blank"));
    }

    @Test
    void answerDateIsRequired(Validator validator) {
        assertThat(validator.validate(new AnswerSave("xxx", null,"I worked on bla bla bla")))
                .anyMatch(x -> x.getPropertyPath().toString().equals("answerDate") && x.getMessage().equals("must not be null"));
    }

    @Test
    void answerDateIsPresentOrPast(Validator validator) {
        LocalDate answerDate = LocalDate.now().plusDays(2);
        assertThat(validator.validate(new AnswerSave("xxx", answerDate, "I worked on bla bla bla")))
                .anyMatch(x -> x.getPropertyPath().toString().equals("answerDate") && x.getMessage().equals("must be a date in the past or in the present"));
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
