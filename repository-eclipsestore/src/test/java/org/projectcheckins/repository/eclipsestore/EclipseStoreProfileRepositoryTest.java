package org.projectcheckins.repository.eclipsestore;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.util.Collections.emptyList;
import static java.util.TimeZone.getDefault;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.ClientAuthentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.exceptions.ProfileNotFoundException;
import org.projectcheckins.core.forms.ProfileUpdate;
import org.projectcheckins.security.UserSave;

@MicronautTest
class EclipseStoreProfileRepositoryTest {

  @Test
  void testCrud(EclipseStoreUser storeUser, EclipseStoreProfileRepository profileRepository) {
    final String email = "email@example.com";
    final Authentication wrongAuth = new ClientAuthentication(email, Map.of("email", "wrong@example.com"));
    final Authentication rightAuth = new ClientAuthentication(email, Map.of("email", email));
    assertThatCode(() -> storeUser.register(new UserSave(
        email, "encodedPassword", emptyList(), getDefault(), MONDAY, true)))
            .doesNotThrowAnyException();
    assertThat(profileRepository.findByAuthentication(rightAuth))
        .hasValueSatisfying(p -> assertThat(p)
            .hasFieldOrPropertyWithValue("firstDayOfWeek", MONDAY)
            .hasFieldOrPropertyWithValue("using24HourClock", true));

    profileRepository.update(rightAuth, new ProfileUpdate(getDefault().getID(), SUNDAY, false));
    assertThat(profileRepository.findByAuthentication(rightAuth))
        .hasValueSatisfying(p -> assertThat(p)
            .hasFieldOrPropertyWithValue("firstDayOfWeek", SUNDAY)
            .hasFieldOrPropertyWithValue("using24HourClock", false));

    assertThatThrownBy(() -> profileRepository.update(wrongAuth, new ProfileUpdate(getDefault().getID(), SUNDAY, true)))
        .isInstanceOf(ProfileNotFoundException.class);
  }
}
