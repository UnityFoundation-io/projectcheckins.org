package org.projectcheckins.email.http;

import io.micronaut.core.annotation.NonNull;

public interface EmailConfirmationControllerConfiguration {

    @NonNull
    String getPath();

    @NonNull
    String getSuccessfulRedirect();

    @NonNull
    String getFailureRedirect();
}
