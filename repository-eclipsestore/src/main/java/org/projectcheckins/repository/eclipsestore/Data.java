package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Data {
    @NonNull
    private List<QuestionEntity> questions = new ArrayList<>();
    @NonNull
    private List<UserEntity> users = new ArrayList<>();
    @NonNull
    private List<ProfileEntity> profiles = new ArrayList<>();

    public List<QuestionEntity> getQuestions() {
        return questions;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public List<ProfileEntity> getProfiles() {
        return profiles;
    }
}
