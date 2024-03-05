package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.authentication.Authentication;

@FunctionalInterface
public interface ProfileRepository {

    Format findPreferedFormat(@NonNull Authentication authentication);
}
