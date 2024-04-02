package org.projectcheckins.core.repositories;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Answer;

import java.util.List;

public interface AnswerRepository {
    @NotBlank
    String save(@NotNull @Valid Answer answer, @Nullable Tenant tenant);

    @NonNull
    List<? extends Answer> findByQuestionId(@NotBlank String questionId, @Nullable Tenant tenant);
}
