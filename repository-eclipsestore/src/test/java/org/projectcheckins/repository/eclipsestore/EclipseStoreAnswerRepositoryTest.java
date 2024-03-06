package org.projectcheckins.repository.eclipsestore;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.forms.AnswerUpdate;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.QuestionSave;
import org.projectcheckins.core.repositories.QuestionRepository;
import org.projectcheckins.security.RegisterService;
import org.projectcheckins.security.UserAlreadyExistsException;

@MicronautTest
class EclipseStoreAnswerRepositoryTest {

    @Test
    void testCrud(RegisterService registerService,
                  QuestionRepository questionRepository,
                  EclipseStoreAnswerRepository answerRepository) throws UserAlreadyExistsException {

        String text = "Lorem ipsum";
        String questionId = questionRepository.save(new QuestionSave("What are you working on?"));
        String userId = registerService.register("delamos@unityfoundation.io", "secret");
        String id = answerRepository.save(new AnswerSave(questionId, Format.MARKDOWN, LocalDate.now(), text), Authentication.build(userId), null);
        assertThat(answerRepository.findByQuestionId(questionId, null))
            .anyMatch(a -> a.html().contains(text));

        assertThat(answerRepository.findByQuestionId(questionId, null))
            .anyMatch(a -> a.id().equals(id));

        String updatedText = "Hello world";
        answerRepository.update(new AnswerUpdate(questionId, id, Format.MARKDOWN,LocalDate.now(), updatedText), null);
        assertThat(answerRepository.findById(id, null))
            .isNotEmpty()
            .hasValueSatisfying(a -> a.html().contains(updatedText));

        answerRepository.deleteById(id, null);
        assertThat(answerRepository.findByQuestionId(questionId, null)).isEmpty();
    }
}
