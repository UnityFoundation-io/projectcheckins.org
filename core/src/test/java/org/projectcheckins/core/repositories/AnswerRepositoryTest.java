package org.projectcheckins.core.repositories;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.ServerAuthentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.AnswerSave;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Property(name = "spec.name", value = "AnswerRepositoryTest")
@MicronautTest(startApplication = false)
class AnswerRepositoryTest {
    @Test
    void testAnswerSaveIsValidated(AnswerRepository answerRepository) {
        AnswerSave answerSave = new AnswerSave("questionId", null, "text");
        Authentication authentication = new ServerAuthentication("user", Collections.emptyList(), Map.of("email", "delamos@unityfoundation.io"));
        assertThrows(ConstraintViolationException.class, () -> answerRepository.save(answerSave, authentication));
    }

    @Test
    void testAuthenticationCannotBeNull(AnswerRepository answerRepository) {
        AnswerSave answerSave = new AnswerSave("xxx", LocalDate.now(), "I worked on bla bla bla");
        Authentication authentication = null;
        assertThrows(ConstraintViolationException.class, () -> answerRepository.save(answerSave, authentication));
    }

    @Requires(property = "spec.name", value = "AnswerRepositoryTest")
    @Singleton
    static class AnswerRepositoryImpl implements AnswerRepository {

        @Override
        public String save(@NonNull @NotNull @Valid AnswerSave answerSave,
                              @NonNull @NotNull Authentication authentication//,
                              // @Nullable Tenant tenant
                              ) {
            throw new UnsupportedOperationException("Not implemented");
        }
    }

}
