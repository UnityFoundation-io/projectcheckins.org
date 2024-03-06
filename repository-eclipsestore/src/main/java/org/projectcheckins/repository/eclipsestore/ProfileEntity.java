package org.projectcheckins.repository.eclipsestore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.TimeZone;
import org.projectcheckins.core.forms.TimeFormat;

public class ProfileEntity {

  @NotBlank
  private String id;

  @NotBlank
  @Email
  private String email;

  @NotNull
  private TimeZone timeZone;

  @NotNull
  private DayOfWeek firstDayOfWeek;

  @NotNull
  private TimeFormat timeFormat;

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

  public TimeFormat getTimeFormat() {
    return timeFormat;
  }

  public void setTimeFormat(TimeFormat timeFormat) {
    this.timeFormat = timeFormat;
  }
}
