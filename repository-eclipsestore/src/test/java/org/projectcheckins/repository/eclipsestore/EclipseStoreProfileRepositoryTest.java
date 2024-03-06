package org.projectcheckins.repository.eclipsestore;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.time.DayOfWeek;
import java.util.TimeZone;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.ProfileSave;
import org.projectcheckins.core.forms.ProfileUpdate;
import org.projectcheckins.security.TimeFormat;

@MicronautTest
class EclipseStoreProfileRepositoryTest {

  @Test
  void testCrud(EclipseStoreProfileRepository profileRepository) {
    final TimeFormat timeFormat = TimeFormat.USE_24_HOUR_CLOCK;
    final String email = "email@example.com";
    final String id = profileRepository.save(new ProfileSave(email,
        TimeZone.getDefault(),
        DayOfWeek.MONDAY,
        timeFormat));
    assertThat(profileRepository.findById(id))
        .hasValueSatisfying(p -> assertThat(p)
            .hasFieldOrPropertyWithValue("email", email)
            .hasFieldOrPropertyWithValue("timeFormat", timeFormat));

    final TimeFormat updatedTimeFormat = TimeFormat.USE_12_HOUR_CLOCK;
    profileRepository.update(new ProfileUpdate(id,
        TimeZone.getDefault().getID(),
        DayOfWeek.MONDAY,
        updatedTimeFormat);
    assertThat(profileRepository.findByEmail(email))
        .hasValueSatisfying(p -> assertThat(p)
            .hasFieldOrPropertyWithValue("id", id)
            .hasFieldOrPropertyWithValue("timeFormat", updatedTimeFormat));

    profileRepository.deleteById(id);
    assertThat(profileRepository.findByEmail(email)).isEmpty();
  }
}
