package org.projectcheckins.repository.eclipsestore;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.forms.AnswerUpdate;

@MicronautTest
class EclipseStoreAnswerRepositoryTest {

    @Test
    void testCrud(EclipseStoreAnswerRepository answerRepository) {

        String text = "Lorem ipsum";
        String id = answerRepository.save(new AnswerSave("xxx", LocalDate.now(), text), null, null);
        assertThat(answerRepository.findByQuestionId("xxx", null))
            .anyMatch(a -> a.text().equals(text));

        assertThat(answerRepository.findByQuestionId("xxx", null))
            .anyMatch(a -> a.id().equals(id));

        String updatedText = "Hello world";
        answerRepository.update(new AnswerUpdate(id, LocalDate.now(), updatedText), null);
        assertThat(answerRepository.findById(id, null))
            .isNotEmpty()
            .hasValueSatisfying(a -> a.text().equals(updatedText));

        answerRepository.deleteById(id, null);
        assertThat(answerRepository.findByQuestionId("xxx", null)).isEmpty();
    }
}
