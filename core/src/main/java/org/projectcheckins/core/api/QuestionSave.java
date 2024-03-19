package org.projectcheckins.core.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.forms.HowOften;
import org.projectcheckins.core.forms.TimeOfDay;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public interface QuestionSave {
    @NotBlank String title();
    @NotNull HowOften howOften();
    @NotEmpty Set<DayOfWeek> days();
    @NotNull TimeOfDay timeOfDay();
    @NotNull LocalTime fixedTime();
}
