package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@FunctionalInterface
public interface QuestionSaveMapper {

    @NonNull
    QuestionSave toQuestionSave(@NonNull @NotNull @Valid QuestionSaveForm questionSaveForm);
}
