package org.projectcheckins.security;

import java.time.DayOfWeek;
import java.util.TimeZone;

public interface UserState {

    String getId();

    boolean isEnabled();

    String getEmail();

    String getPassword();

    TimeZone getTimeZone();

    DayOfWeek getFirstDayOfWeek();

    boolean isUsing24HourClock();
}
