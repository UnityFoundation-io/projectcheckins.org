package org.projectcheckins.repository.eclipsestore;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.time.DayOfWeek;
import java.util.TimeZone;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Appearance;
import org.projectcheckins.core.forms.ProfileSave;
import org.projectcheckins.core.forms.ProfileUpdate;
import org.projectcheckins.core.forms.TimeFormat;

@MicronautTest
class EclipseStoreProfileRepositoryTest {

  @Test
  void testCrud(EclipseStoreProfileRepository profileRepository) {
    final Appearance appearance = Appearance.ALWAYS_DARK;
    final String email = "email@example.com";
    final String id = profileRepository.save(new ProfileSave(email,
        appearance,
        TimeZone.getDefault(),
        DayOfWeek.MONDAY,
        TimeFormat.USE_24_HOUR_CLOCK));
    assertThat(profileRepository.findById(id))
        .hasValueSatisfying(p -> assertThat(p)
            .hasFieldOrPropertyWithValue("email", email)
            .hasFieldOrPropertyWithValue("appearance", appearance));

    final Appearance updatedAppearance = Appearance.ALWAYS_LIGHT;
    profileRepository.update(new ProfileUpdate(id,
        updatedAppearance,
        TimeZone.getDefault().getID(),
        DayOfWeek.MONDAY,
        TimeFormat.USE_24_HOUR_CLOCK));
    assertThat(profileRepository.findByEmail(email))
        .hasValueSatisfying(p -> assertThat(p)
            .hasFieldOrPropertyWithValue("id", id)
            .hasFieldOrPropertyWithValue("appearance", updatedAppearance));

    profileRepository.deleteById(id);
    assertThat(profileRepository.findByEmail(email)).isEmpty();
  }
}
