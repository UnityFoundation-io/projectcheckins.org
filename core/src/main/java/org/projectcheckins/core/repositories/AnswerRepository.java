package org.projectcheckins.core.repositories;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.validation.constraints.PastOrPresent;
import org.projectcheckins.core.forms.Answer;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.forms.AnswerUpdate;

public interface AnswerRepository {

    @NonNull
    Optional<Answer> findById(@NotBlank String answerId, @Nullable Tenant tenant);

    @NonNull
    List<Answer> findByQuestionId(@NotBlank String questionId, @Nullable Tenant tenant);

    @NonNull
    default String save(@NonNull @NotNull @Valid AnswerSave answerSave,
                @NonNull Authentication authentication) {
        return save(answerSave, authentication, null);
    }

    @NonNull
    String save(@NonNull @NotNull @Valid AnswerSave answerSave,
                @NonNull Authentication authentication,
                @Nullable Tenant tenant);

    void update(@NotNull @Valid AnswerUpdate answerUpdate, @Nullable Tenant tenant);

    void deleteById(@NotBlank String id, @Nullable Tenant tenant);

    @NonNull
    Optional<AnswerUpdate> findAnswerUpdate(@NotBlank String questionId,
                                            @NotBlank String id,
                                            @Nullable Tenant tenant);

    boolean hasAnswered(@NotBlank String questionId,
                        @NotNull@PastOrPresent LocalDate answerDate,
                        @NonNull @NotNull Authentication authentication,
                        @Nullable Tenant tenant);
}
