package org.projectcheckins.core.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.projectcheckins.core.forms.HowOften;
import org.projectcheckins.core.forms.TimeOfDay;

import java.time.DayOfWeek;
import java.util.List;

public interface Question {

    @NotBlank String id();

    @NotBlank String title();

    @NotNull HowOften howOften();
    @NotNull @Size(min = 1, max = 7) List<DayOfWeek> days();
    @NotNull TimeOfDay timeOfDay();
}
