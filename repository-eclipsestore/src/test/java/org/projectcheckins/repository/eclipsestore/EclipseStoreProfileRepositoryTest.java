package org.projectcheckins.repository.eclipsestore;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.util.Collections.emptyList;
import static java.util.TimeZone.getDefault;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.exceptions.ProfileNotFoundException;
import org.projectcheckins.core.forms.ProfileUpdate;
import org.projectcheckins.security.UserSave;

@MicronautTest
class EclipseStoreProfileRepositoryTest {

  @Test
  void testCrud(EclipseStoreUser storeUser, EclipseStoreProfileRepository profileRepository) {
    final String email = "email@example.com";
    assertThatCode(() -> storeUser.register(new UserSave(
        email, "encodedPassword", emptyList(), getDefault(), MONDAY, true)))
            .doesNotThrowAnyException();
    assertThat(profileRepository.findByEmail(email))
        .hasValueSatisfying(p -> assertThat(p)
            .hasFieldOrPropertyWithValue("firstDayOfWeek", MONDAY)
            .hasFieldOrPropertyWithValue("using24HourClock", true));

    profileRepository.update(email, new ProfileUpdate(getDefault().getID(), SUNDAY, false));
    assertThat(profileRepository.findByEmail(email))
        .hasValueSatisfying(p -> assertThat(p)
            .hasFieldOrPropertyWithValue("firstDayOfWeek", SUNDAY)
            .hasFieldOrPropertyWithValue("using24HourClock", false));

    assertThatThrownBy(() -> profileRepository.update("wrong@example.com", new ProfileUpdate(getDefault().getID(), SUNDAY, true)))
        .isInstanceOf(ProfileNotFoundException.class);
  }
}
