package org.projectcheckins.core.api;

import io.micronaut.core.annotation.NonNull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.FullNameUtils;
import org.projectcheckins.core.forms.TimeFormat;
import org.projectcheckins.security.api.PublicProfile;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.TimeZone;

public interface Profile extends PublicProfile {

    @NotNull TimeZone timeZone();

    @NotNull DayOfWeek firstDayOfWeek();

    @NotNull LocalTime beginningOfDay();

    @NotNull LocalTime endOfDay();

    @NotNull TimeFormat timeFormat();

    @NotNull Format format();

    @Nullable
    String firstName();

    @Nullable
    String lastName();

    @NonNull
    default String fullName() {
        return FullNameUtils.getFullName(firstName(), lastName());
    }
}
