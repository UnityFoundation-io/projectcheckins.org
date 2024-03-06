package org.projectcheckins.http.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.projectcheckins.http.AssertUtils.htmlBody;
import static org.projectcheckins.http.AssertUtils.htmlPage;
import static org.projectcheckins.http.AssertUtils.redirection;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import java.time.DayOfWeek;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Profile;
import org.projectcheckins.core.forms.ProfileSave;
import org.projectcheckins.core.forms.ProfileUpdate;
import org.projectcheckins.core.forms.TimeFormat;
import org.projectcheckins.core.repositories.ProfileRepository;
import org.projectcheckins.http.BrowserRequest;

@Property(name = "micronaut.security.filter.enabled", value = StringUtils.FALSE)
@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@Property(name = "spec.name", value = "ProfileControllerTest")
@MicronautTest
class ProfileControllerTest {

  @Test
  void crud(@Client("/") HttpClient httpClient) {
    BlockingHttpClient client = httpClient.toBlocking();

    assertThat(client.exchange(BrowserRequest.GET("/profile/show"), String.class))
        .matches(htmlPage())
        .matches(htmlBody("/profile/edit"));

    assertThat(client.exchange(BrowserRequest.GET("/profile/edit"), String.class))
        .matches(htmlPage())
        .matches(htmlBody("/profile/update"));

    assertThat(client.exchange(BrowserRequest.POST("/profile/update", Map.of(
        "id", "ID",
        "timeZone", TimeZone.getDefault().getID(),
        "firstDayOfWeek", DayOfWeek.MONDAY.name(),
        "timeFormat", TimeFormat.USE_24_HOUR_CLOCK.name()))))
        .matches(redirection("/profile/show"));
  }

  @Requires(property = "spec.name", value = "ProfileControllerTest")
  @Replaces(AuthenticationHelper.class)
  @Singleton
  public static class AuthenticationHelperMock extends AuthenticationHelper {
    @NonNull
    public Optional<String> getEmailAttribute(@Nullable Authentication authentication) {
      return Optional.of("email@example.com");
    }
  }

  @Requires(property = "spec.name", value = "ProfileControllerTest")
  @Singleton
  @Replaces(ProfileRepository.class)
  static class ProfileRepositoryMock implements ProfileRepository {
    @Override
    public String save(ProfileSave profileSave, Tenant tenant) {
      return "ID";
    }

    @Override
    public Optional<Profile> findById(String id, Tenant tenant) {
      if (id.equals("ID")) {
        return Optional.of(new Profile("ID",
            "email@example.com",
            TimeZone.getDefault().getID(),
            DayOfWeek.MONDAY,
            TimeFormat.USE_24_HOUR_CLOCK));
      }
      return Optional.empty();
    }

    @Override
    public Optional<Profile> findByEmail(String email, Tenant tenant) {
      if (email.equals("email@example.com")) {
        return Optional.of(new Profile("ID",
            "email@example.com",
            TimeZone.getDefault().getID(),
            DayOfWeek.MONDAY,
            TimeFormat.USE_24_HOUR_CLOCK));
      }
      return Optional.empty();
    }

    @Override
    public void update(ProfileUpdate profileUpdate, Tenant tenant) {

    }

    @Override
    public void deleteById(String id, Tenant tenant) {

    }
  }
}
