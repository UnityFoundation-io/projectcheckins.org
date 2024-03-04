package org.projectcheckins.http.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.projectcheckins.http.AssertUtils.redirection;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.Answer;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.forms.AnswerUpdate;
import org.projectcheckins.core.repositories.AnswerRepository;
import org.projectcheckins.http.BrowserRequest;

@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@Property(name = "micronaut.security.filter.enabled", value = StringUtils.FALSE)
@Property(name = "spec.name", value = "AnswerControllerFormTest")
@MicronautTest
class AnswerControllerFormTest {
    @Test
    void crud(@Client("/") HttpClient httpClient, AnswerRepository answerRepository) {
        BlockingHttpClient client = httpClient.toBlocking();
        String text = "Lorem ipsum";
        HttpResponse<?> saveResponse = client.exchange(BrowserRequest.POST("/question/xxx/answer/save", Map.of(
            "questionId", "xxx", "answerDate", "2024-01-01", "text", text)));
        assertThat(saveResponse)
            .matches(redirection("/question/xxx/answer/list"));

        assertThatThrownBy(() -> client.exchange(BrowserRequest.POST("/question/xxx/answer/save", Map.of(
            "questionId", "different", "answerDate", "2024-01-01", "text", text))))
            .isInstanceOf(HttpClientResponseException.class)
            .extracting(e -> ((HttpClientResponseException)e).getStatus())
            .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        assertThat(client.retrieve(BrowserRequest.GET("/question/xxx/answer/list"), String.class))
            .contains("/question/xxx/answer/yyy/show");

        assertThat(client.retrieve(BrowserRequest.GET("/question/xxx/answer/yyy/show"), String.class))
            .contains(text)
            .contains("/question/xxx/answer/yyy/edit");

        assertThat(client.retrieve(BrowserRequest.GET("/question/xxx/answer/yyy/edit"), String.class))
            .contains(text);

        String updatedText = "Hello world";
        assertThat(client.exchange(BrowserRequest.POST("/question/xxx/answer/yyy/update", Map.of(
            "id", "yyy", "answerDate", "2024-01-01", "text", updatedText))))
            .matches(redirection(s -> s.equals("/question/xxx/answer/yyy/show")));

        assertThat(client.retrieve(BrowserRequest.GET("/question/xxx/answer/yyy/edit"), String.class))
            .doesNotContain(text)
            .contains(updatedText);

        assertThatThrownBy(() -> client.exchange(BrowserRequest.POST("/question/xxx/answer/yyy/update", Map.of(
            "id", "zzz", "answerDate", "2024-01-01", "text", updatedText))))
            .isInstanceOf(HttpClientResponseException.class)
            .extracting(e -> ((HttpClientResponseException)e).getStatus())
            .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        assertThat(client.exchange(BrowserRequest.POST("/question/xxx/answer/yyy/delete", Collections.emptyMap())))
            .matches(redirection("/question/xxx/answer/list"));

        assertThat(answerRepository.findByQuestionId("xxx", null)).isEmpty();
    }

    @Requires(property = "spec.name", value = "AnswerControllerFormTest")
    @Singleton
    @Replaces(AnswerRepository.class)
    static class AnswerRepositoryMock implements AnswerRepository {

        Map<String, Answer> answers = new HashMap<>();

        @Override
        public Optional<Answer> findById(String answerId, @Nullable Tenant tenant) {
            return Optional.ofNullable(answers.get(answerId));
        }

        @Override
        public List<Answer> findByQuestionId(String questionId, @Nullable Tenant tenant) {
            return answers.values().stream().toList();
        }

        @Override
        public String save(AnswerSave answerSave, @Nullable Authentication authentication, @Nullable Tenant tenant) {
            String id = "yyy";
            answers.put(id, new Answer(id, "xxx", "user", answerSave.answerDate(), answerSave.text()));
            return id;
        }

        @Override
        public void update(AnswerUpdate answerUpdate, @Nullable Tenant tenant) {
            final String id = answerUpdate.id();
            if (answers.containsKey(id)) {
                answers.put(id, new Answer(id, "xxx", "user", answerUpdate.answerDate(), answerUpdate.text()));
            }
        }

        @Override
        public void deleteById(String id, @Nullable Tenant tenant) {
            answers.remove(id);
        }
    }
}
