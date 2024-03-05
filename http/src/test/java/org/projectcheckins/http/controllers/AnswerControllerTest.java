package org.projectcheckins.http.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.projectcheckins.http.AssertUtils.htmlBody;
import static org.projectcheckins.http.AssertUtils.htmlPage;
import static org.projectcheckins.http.AssertUtils.redirection;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.*;
import org.projectcheckins.core.models.Element;
import org.projectcheckins.core.repositories.AnswerRepository;
import org.projectcheckins.core.repositories.QuestionRepository;
import org.projectcheckins.http.BrowserRequest;

@Property(name = "micronaut.security.filter.enabled", value = StringUtils.FALSE)
@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@Property(name = "spec.name", value = "AnswerControllerTest")
@MicronautTest
class AnswerControllerTest {

    private static final String QUESTION_ID = "xxx";

    @Test
    void crud(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        assertThat(client.exchange(BrowserRequest.GET("/question/" + QUESTION_ID + "/answer/list"), String.class))
            .matches(htmlPage());

        assertThat(client.exchange(BrowserRequest.GET("/question/" + QUESTION_ID + "/answer/yyy/edit"), String.class))
            .matches(htmlPage());

        assertThat(client.exchange(BrowserRequest.GET("/question/" + QUESTION_ID + "/answer/zzz/edit"), String.class))
            .matches(redirection("/notFound"));

        assertThat(client.exchange(BrowserRequest.GET("/question/" + QUESTION_ID + "/answer/yyy/show"), String.class))
            .matches(htmlPage());
    }

    @Requires(property = "spec.name", value = "AnswerControllerTest")
    @Singleton
    @Replaces(QuestionRepository.class)
    static class QuestionRepositoryMock implements QuestionRepository {

        @Override
        public String save(QuestionSave questionSave, Tenant tenant) {
            return null;
        }

        @Override
        public Optional<Question> findById(String id, Tenant tenant) {
            return Optional.empty();
        }

        @Override
        public void update(QuestionUpdate questionUpdate, Tenant tenant) {

        }

        @Override
        public List<Question> findAll(Tenant tenant) {
            return null;
        }

        @Override
        public void deleteById(String id, Tenant tenant) {

        }

        @Override
        public Optional<Element> findElementById(String questionId, Tenant tenant) {
            if (questionId.equals(QUESTION_ID)) {
                return Optional.of(new Element(QUESTION_ID, "What are working on?"));
            }
            return Optional.empty();
        }
    }

    @Requires(property = "spec.name", value = "AnswerControllerTest")
    @Singleton
    @Replaces(AnswerRepository.class)
    static class AnswerRepositoryMock implements AnswerRepository {
        @NonNull
        public Optional<AnswerUpdate> findAnswerUpdate(@NotBlank String questionId,
                                                       @NotBlank String id,
                                                       @Nullable Tenant tenant) {
            return Optional.empty();
        }

        @Override
        public Optional<Answer> findById(String answerId, @Nullable Tenant tenant) {
            if (answerId.equals("yyy")) {
                return Optional.of(new Answer("yyy", "xxx",new Element("zzz", "user"), LocalDate.now(), "Lorem ipsum"));
            }
            return Optional.empty();
        }

        @Override
        public List<Answer> findByQuestionId(String questionId, @Nullable Tenant tenant) {
            return Collections.emptyList();
        }

        @Override
        public String save(AnswerSave answerSave, @Nullable Authentication authentication, @Nullable Tenant tenant) {
            return "yyy";
        }

        @Override
        public void update(AnswerUpdate answerUpdate, @Nullable Tenant tenant) {

        }

        @Override
        public void deleteById(String id, @Nullable Tenant tenant) {

        }

        @Override
        public boolean hasAnswered(@NotBlank String questionId,
                                   @NotNull @PastOrPresent LocalDate answerDate,
                                   @NonNull @NotNull Authentication authentication,
                                   @Nullable Tenant tenant) {
            return false;
        }
    }
}
