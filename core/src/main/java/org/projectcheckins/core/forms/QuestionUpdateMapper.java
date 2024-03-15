package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@FunctionalInterface
public interface QuestionUpdateMapper {
    @NonNull
    QuestionUpdate toQuestionUpdate(@NonNull @NotNull @Valid QuestionUpdateForm questionSaveForm);
}
