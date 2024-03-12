package org.projectcheckins.core.services;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CronCalculatorTest {

    @Test
    void calculateCron() {
        CronCalculator cronCalculator = new CronCalculatorImpl();
        String cron = cronCalculator.calculateCron(Frequency.DAILY_ON,
                List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY),
                LocalTime.of(16, 30));
        assertEquals("30 16 * * MON-FRI", cron);


        cron = cronCalculator.calculateCron(Frequency.ONCE_A_WEEK,
                Collections.singletonList(DayOfWeek.MONDAY),
                LocalTime.of(9, 0));
        assertEquals("0 9 * * MON", cron);
    }
}