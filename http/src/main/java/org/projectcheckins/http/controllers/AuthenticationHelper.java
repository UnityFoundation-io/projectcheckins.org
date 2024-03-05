package org.projectcheckins.http.controllers;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import java.util.Optional;

@Singleton
public class AuthenticationHelper {

  public static final String ATTRIBUTES_EMAIL = "email";

  @NonNull
  public Optional<String> getEmailAttribute(@Nullable Authentication authentication) {
    return Optional.ofNullable(authentication)
        .map(x -> x.getAttributes().get(ATTRIBUTES_EMAIL))
        .map(Object::toString);
  }
}
