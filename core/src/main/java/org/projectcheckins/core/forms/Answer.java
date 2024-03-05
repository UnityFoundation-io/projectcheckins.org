package org.projectcheckins.core.forms;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.projectcheckins.core.models.Element;

import java.time.LocalDate;

@Serdeable
public record Answer(@NotBlank String id,
                     @NotBlank String questionId,
                     @NotBlank Element user,
                     @NotNull @PastOrPresent LocalDate answerDate,
                     @NotBlank String html) {
}
