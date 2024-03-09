package org.projectcheckins.repository.eclipsestore;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.util.TimeZone;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class UserEntityTest {

    @Test
    void settersAndGetters() {
        UserEntity user = new UserEntity("id", "email@projectcheckins.org", "password", Collections.emptyList(), TimeZone.getDefault(), DayOfWeek.SUNDAY, false);
        assertThat(user)
            .hasFieldOrPropertyWithValue("id", "id")
            .hasFieldOrPropertyWithValue("email", "email@projectcheckins.org")
            .hasFieldOrPropertyWithValue("encodedPassword", "password")
            .hasFieldOrPropertyWithValue("enabled", false)
            .satisfies(u -> assertThat(u.getAuthorities()).isEmpty())
            .hasFieldOrPropertyWithValue("timeZone", TimeZone.getDefault())
            .hasFieldOrPropertyWithValue("firstDayOfWeek", DayOfWeek.SUNDAY)
            .hasFieldOrPropertyWithValue("using24HourClock", false);

        user.setId("newId");
        user.setEmail("newEmail@projectcheckins.org");
        user.setEnabled(true);
        user.setEncodedPassword("newPassword");
        user.setAuthorities(Collections.singletonList("ROLE_USER"));
        user.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        user.setFirstDayOfWeek(DayOfWeek.MONDAY);
        user.setUsing24HourClock(true);

        assertThat(user)
            .hasFieldOrPropertyWithValue("id", "newId")
            .hasFieldOrPropertyWithValue("email", "newEmail@projectcheckins.org")
            .hasFieldOrPropertyWithValue("encodedPassword", "newPassword")
            .hasFieldOrPropertyWithValue("enabled", true)
            .satisfies(u -> assertThat(u.getAuthorities()).containsExactly("ROLE_USER"))
            .hasFieldOrPropertyWithValue("timeZone", TimeZone.getTimeZone("Europe/Madrid"))
            .hasFieldOrPropertyWithValue("firstDayOfWeek", DayOfWeek.MONDAY)
            .hasFieldOrPropertyWithValue("using24HourClock", true);
    }

}