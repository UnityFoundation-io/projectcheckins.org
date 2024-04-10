package org.projectcheckins.core.services;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.AnswerView;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.models.DateAnswers;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface AnswerService {

    @NonNull
    String save(@NotNull Authentication authentication, @NotNull @Valid AnswerSave answerSave, @Nullable Tenant tenant);

    @NonNull
    Optional<? extends AnswerView> findById(@NotBlank String id, @NotNull Authentication authentication, @Nullable Tenant tenant);

    @NonNull
    List<DateAnswers> findByQuestionId(@NotBlank String questionId, @NotNull Authentication authentication, @Nullable Tenant tenant);

    @NonNull
    String getAnswerSummary(@NotNull AnswerView answerView, @Nullable Locale locale);
}
