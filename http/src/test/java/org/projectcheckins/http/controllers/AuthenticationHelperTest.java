package org.projectcheckins.http.controllers;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.security.authentication.ClientAuthentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.util.Map;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false)
class AuthenticationHelperTest {

  @Test
  void handleNull(AuthenticationHelper helper) {
    assertThat(helper.getEmailAttribute(null))
        .isEmpty();
  }

  @Test
  void handleEmptyMap(AuthenticationHelper helper) {
    assertThat(helper.getEmailAttribute(new ClientAuthentication("name", emptyMap())))
        .isEmpty();
  }

  @Test
  void handleEmail(AuthenticationHelper helper) {
    assertThat(helper.getEmailAttribute(new ClientAuthentication("name", Map.of("email", "email@example.com"))))
        .contains("email@example.com");
  }
}
