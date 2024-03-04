package org.projectcheckins.core.repositories;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.ServerAuthentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Answer;
import org.projectcheckins.core.forms.AnswerSave;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import org.projectcheckins.core.forms.AnswerUpdate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Property(name = "spec.name", value = "AnswerRepositoryTest")
@MicronautTest(startApplication = false)
class AnswerRepositoryTest {
    @Test
    void testAnswerSaveIsValidated(AnswerRepository answerRepository) {
        AnswerSave answerSave = new AnswerSave("questionId", null, "text");
        Authentication authentication = new ServerAuthentication("user", Collections.emptyList(), Map.of("email", "delamos@unityfoundation.io"));
        assertThrows(ConstraintViolationException.class, () -> answerRepository.save(answerSave, authentication, null));
    }

    @Requires(property = "spec.name", value = "AnswerRepositoryTest")
    @Singleton
    static class AnswerRepositoryImpl implements AnswerRepository {

        @Override
        @NonNull
        public Optional<Answer> findById(@NotBlank String answerId, @Nullable Tenant tenant) {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        @NonNull
        public List<Answer> findByQuestionId(@NotBlank String questionId, @Nullable Tenant tenant) {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        @NonNull
        public String save(@NonNull @NotNull @Valid AnswerSave answerSave,
                              @Nullable Authentication authentication,
                              @Nullable Tenant tenant) {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        public void update(@NotNull @Valid AnswerUpdate answerUpdate, @Nullable Tenant tenant) {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        public void deleteById(@NotBlank String id, @Nullable Tenant tenant) {
            throw new UnsupportedOperationException("Not implemented");
        }
    }

}
