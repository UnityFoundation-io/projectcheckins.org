package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.QuestionUpdate;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.Set;

@Serdeable
public record QuestionUpdateForm(@NonNull String id,
                                 @NotBlank String title,
                                 @NotNull HowOften howOften,
                                 @NotNull TimeOfDay timeOfDay,
                                 @NotEmpty Set<DayOfWeek> manyDays,
                                 @Nullable DayOfWeek oneDay) implements QuestionUpdate {
    @Override
    public Set<DayOfWeek> days() {
        return switch (howOften) {
            case DAILY_ON -> manyDays;
            default -> Collections.singleton(oneDay);
        };
    }
}
