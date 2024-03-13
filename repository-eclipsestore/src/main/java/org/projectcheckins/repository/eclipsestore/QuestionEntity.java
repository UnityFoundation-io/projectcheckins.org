package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class QuestionEntity {
    @NotBlank
    private String id;

    @NotBlank
    private String title;

    @NonNull
    private List<AnswerEntity> answers = new ArrayList<>();

    @NotBlank
    private String schedule;

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

    public List<AnswerEntity> getAnswers() {
        return answers;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
