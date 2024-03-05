package org.projectcheckins.core.repositories;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.forms.Question;
import org.projectcheckins.core.forms.QuestionSave;
import org.projectcheckins.core.forms.QuestionUpdate;
import org.projectcheckins.core.models.Element;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {

    @NonNull
    String save(@NotNull @Valid QuestionSave questionSave, @Nullable Tenant tenant);

    @NonNull
    Optional<Question> findById(@NotBlank String id, @Nullable Tenant tenant);

    void update(@NotNull @Valid QuestionUpdate questionUpdate, @Nullable Tenant tenant);

    @NonNull
    List<Question> findAll(@Nullable Tenant tenant);

    void deleteById(@NotBlank String id, @Nullable Tenant tenant);

    @NonNull
    default String save(@NotNull @Valid QuestionSave questionSave) {
        return save(questionSave, null);
    }

    @NonNull
    default Optional<Question> findById(@NotBlank String id) {
        return findById(id, null);
    }

    default void update(@NotNull @Valid QuestionUpdate questionUpdate) {
        update(questionUpdate, null);
    }

    @NonNull
    default List<Question> findAll() {
        return findAll(null);
    }

    default void deleteById(@NotBlank String id) {
        deleteById(id, null);
    }

    @NonNull
    Optional<Element> findElementById(@NotBlank String questionId,
                                      @Nullable Tenant tenant);
}
