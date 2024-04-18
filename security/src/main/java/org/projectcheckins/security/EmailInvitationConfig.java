package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;

public interface EmailInvitationConfig {

    @NonNull
    String getUrl();
}
