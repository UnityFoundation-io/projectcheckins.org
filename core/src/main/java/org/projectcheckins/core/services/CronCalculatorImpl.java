package org.projectcheckins.core.services;

import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Singleton
public class CronCalculatorImpl implements CronCalculator {
    @Override
    public String calculateCron(@NonNull Frequency frequency,
                                @NonNull List<DayOfWeek> days,
                                @NonNull LocalTime time) {
        return null;
    }
}
