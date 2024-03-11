package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class FullNameUtils {
    private FullNameUtils() {
    }

    @NonNull
    public static String getFullName(@Nullable String firstName,
                                     @Nullable String lastName) {
        List<String> parts = new ArrayList<>();
        if (firstName != null) {
            parts.add(firstName);
        }
        if (lastName != null) {
            parts.add(lastName);
        }
        return String.join(" ", parts);
    }
}
