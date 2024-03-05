package org.projectcheckins.core.repositories;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Answer;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.forms.AnswerUpdate;
import org.projectcheckins.core.forms.Format;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Property(name = "spec.name", value = "AnswerRepositoryTest")
@MicronautTest(startApplication = false)
class AnswerRepositoryTest {
    @Test
    void testAnswerSaveIsValidated(AnswerRepository answerRepository) {
        AnswerSave answerSave = new AnswerSave("questionId", Format.MARKDOWN, null, "text");
        Authentication authentication = Authentication.build("user", Collections.emptyList(), Map.of("email", "delamos@unityfoundation.io"));
        assertThrows(ConstraintViolationException.class, () -> answerRepository.save(answerSave, authentication, null));
    }

    @Requires(property = "spec.name", value = "AnswerRepositoryTest")
    @Singleton
    static class AnswerRepositoryImpl implements AnswerRepository {

        @NonNull
        public Optional<AnswerUpdate> findAnswerUpdate(@NotBlank String questionId,
                                                @NotBlank String id,
                                                @Nullable Tenant tenant) {
            return Optional.empty();
        }

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

        @Override
        public boolean hasAnswered(@NotBlank String questionId,
                                   @NotNull@PastOrPresent LocalDate answerDate,
                                   @NonNull @NotNull Authentication authentication,
                                   @Nullable Tenant tenant) {
            return false;
        }
    }

}
