package org.projectcheckins.http.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.projectcheckins.http.AssertUtils.htmlBody;
import static org.projectcheckins.http.AssertUtils.htmlPage;
import static org.projectcheckins.http.AssertUtils.redirection;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
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
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Answer;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.forms.AnswerUpdate;
import org.projectcheckins.core.repositories.AnswerRepository;
import org.projectcheckins.http.BrowserRequest;

@Property(name = "micronaut.security.filter.enabled", value = StringUtils.FALSE)
@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@Property(name = "spec.name", value = "AnswerControllerTest")
@MicronautTest
class AnswerControllerTest {

    @Test
    void crud(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        assertThat(client.exchange(BrowserRequest.GET("/question/xxx/answer/list"), String.class))
            .matches(htmlPage());

        assertThat(client.exchange(BrowserRequest.GET("/question/xxx/answer/yyy/edit"), String.class))
            .matches(htmlPage());

        assertThat(client.exchange(BrowserRequest.GET("/question/xxx/answer/zzz/edit"), String.class))
            .matches(redirection("/notFound"));

        assertThat(client.exchange(BrowserRequest.GET("/question/xxx/answer/yyy/show"), String.class))
            .matches(htmlPage());

        assertThat(client.exchange(BrowserRequest.GET("/question/xxx/answer/create"), String.class))
            .matches(htmlPage())
            .matches(htmlBody("""
                <input type="text" name="title"""));

    }

    @Requires(property = "spec.name", value = "AnswerControllerTest")
    @Singleton
    @Replaces(AnswerRepository.class)
    static class AnswerRepositoryMock implements AnswerRepository {
        @Override
        public Optional<Answer> findById(String answerId, @Nullable Tenant tenant) {
            if (answerId.equals("yyy")) {
                return Optional.of(new Answer("yyy", "xxx", "user", LocalDate.now(), "Lorem ipsum"));
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
    }
}
