package org.projectcheckins.core.services;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.PublicProfile;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.forms.QuestionForm;

import java.util.List;
import java.util.Optional;

public interface QuestionService {

    @NonNull
    String createQuestion(@NotNull @Valid QuestionForm form, @Nullable Tenant tenant);

    @NonNull
    Optional<? extends Question> getQuestion(@NotBlank String id, @Nullable Tenant tenant);

    void updateQuestion(@NotBlank String id, @NotNull @Valid QuestionForm form, @Nullable Tenant tenant);

    @NonNull
    List<? extends Question> listQuestions(@Nullable Tenant tenant);

    void deleteQuestion(@NotBlank String id, @Nullable Tenant tenant);

    @NonNull
    List<? extends PublicProfile> listAvailableRespondents(@Nullable Tenant tenant);
}
