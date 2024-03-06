package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Introspected
public class UserEntity {
    private @NotBlank String id;

    @NotBlank
    private String email;

    boolean enabled;
    @NotBlank
    private String encodedPassword;
    @NonNull List<String> authorities = new ArrayList<>();

    @NotNull
    private TimeZone timeZone;

    @NotNull
    private DayOfWeek firstDayOfWeek;

    private boolean using24HourClock;

    public UserEntity(String id, String email, String encodedPassword, List<String> authorities, TimeZone timeZone, DayOfWeek firstDayOfWeek, boolean using24HourClock) {
        this.id = id;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.authorities = authorities;
        this.timeZone = timeZone;
        this.firstDayOfWeek = firstDayOfWeek;
        this.using24HourClock = using24HourClock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public DayOfWeek getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(DayOfWeek firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public boolean isUsing24HourClock() {
        return using24HourClock;
    }

    public void setUsing24HourClock(boolean using24HourClock) {
        this.using24HourClock = using24HourClock;
    }
}
