package org.projectcheckins.core.services;

import io.micronaut.core.annotation.NonNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public interface CronCalculator {

    @NonNull
    String calculateCron(@NonNull Frequency frequency,
                         @NonNull List<DayOfWeek> days,
                         @NonNull LocalTime time);
}
