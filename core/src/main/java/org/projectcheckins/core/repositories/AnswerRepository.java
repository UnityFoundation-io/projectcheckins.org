package org.projectcheckins.core.repositories;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.authentication.Authentication;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.forms.AnswerSave;

public interface AnswerRepository {

    @NonNull
    String save(@NonNull @NotNull @Valid AnswerSave answerSave,
                @NonNull @NotNull Authentication authentication//,
                          // @Nullable Tenant tenant
                );
}
