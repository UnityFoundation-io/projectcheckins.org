package org.projectcheckins.repository.eclipsestore;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.ClientAuthentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.forms.Format;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatCode;

@MicronautTest
class EclipseStoreAnswerRepositoryTest {

    @Test
    void testCrud(EclipseStoreAnswerRepository answerRepository) {
        final Authentication auth = new ClientAuthentication("user1", null);
        final AnswerSave answer = new AnswerSave("questionId", LocalDate.now(), Format.MARKDOWN, "Lorem ipsum");
        assertThatCode(() -> answerRepository.save(auth, answer))
                .doesNotThrowAnyException();
    }
}
