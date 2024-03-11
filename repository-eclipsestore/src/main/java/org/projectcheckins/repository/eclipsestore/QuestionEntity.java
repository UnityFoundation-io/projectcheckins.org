package org.projectcheckins.repository.eclipsestore;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class QuestionEntity {
    @NotBlank
    private String id;

    @NotBlank
    private String title;

    @NotBlank
    private String schedule;

    @NotNull
    private TimeZone timeZone;

    @Nullable
    private ZonedDateTime nextExecution;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    @Nullable
    public ZonedDateTime getNextExecution() {
        return nextExecution;
    }

    public void setNextExecution(@Nullable ZonedDateTime nextExecution) {
        this.nextExecution = nextExecution;
    }
}
