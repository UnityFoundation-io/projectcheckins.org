package org.projectcheckins.http.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.projectcheckins.http.AssertUtils.redirection;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;

import java.net.URI;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.*;
import org.projectcheckins.core.idgeneration.IdGenerator;
import org.projectcheckins.core.models.Element;
import org.projectcheckins.core.repositories.AnswerRepository;
import org.projectcheckins.core.repositories.QuestionRepository;
import org.projectcheckins.http.BrowserRequest;

@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@Property(name = "micronaut.security.filter.enabled", value = StringUtils.FALSE)
@Property(name = "spec.name", value = "AnswerControllerFormTest")
@MicronautTest
class AnswerControllerFormTest {

    private static final String QUESTION_ID = "xxx";

    @Test
    void crud(@Client("/") HttpClient httpClient, AnswerRepository answerRepository) {
        BlockingHttpClient client = httpClient.toBlocking();
        String text = "Lorem ipsum";
        URI saveUri = UriBuilder.of("/question").path(QUESTION_ID).path("answer").path("save").build();
        URI listUri = UriBuilder.of("/question").path(QUESTION_ID).path("answer").path("list").build();
        HttpResponse<?> saveResponse = client.exchange(BrowserRequest.POST(saveUri.toString(), Map.of(
            "questionId", "xxx", "answerDate", "2024-01-01", "text", text)));
        assertThat(saveResponse)
            .matches(redirection(listUri.toString()));

        assertThatThrownBy(() -> client.exchange(BrowserRequest.POST(saveUri.toString(), Map.of(
            "questionId", "different", "answerDate", "2024-01-01", "text", text))))
            .isInstanceOf(HttpClientResponseException.class)
            .extracting(e -> ((HttpClientResponseException)e).getStatus())
            .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        String answerId = "yyy";
        URI showUri =UriBuilder.of("/question").path(QUESTION_ID).path("answer").path(answerId).path("show").build();

        URI editUri =UriBuilder.of("/question").path(QUESTION_ID).path("answer").path(answerId).path("edit").build();
        assertThat(client.retrieve(BrowserRequest.GET(showUri), String.class))
            .contains(text)
            .contains(editUri.toString());

        assertThat(client.retrieve(BrowserRequest.GET(editUri), String.class))
            .contains(text);

        String updatedText = "Hello world";
        URI updateUri =UriBuilder.of("/question").path(QUESTION_ID).path("answer").path(answerId).path("update").build();
        assertThat(client.exchange(BrowserRequest.POST(updateUri.toString(), Map.of(
            "questionId", "xxx", "id", "yyy", "answerDate", "2024-01-01", "text", updatedText))))
            .matches(redirection(s -> s.equals(showUri.toString())));

        assertThat(client.retrieve(BrowserRequest.GET("/question/xxx/answer/yyy/edit"), String.class))
            .doesNotContain(text)
            .contains(updatedText);

        assertThatThrownBy(() -> client.exchange(BrowserRequest.POST(updateUri.toString(), Map.of(
                "questionId", "xxx","id", "zzz", "answerDate", "2024-01-01", "text", updatedText))))
            .isInstanceOf(HttpClientResponseException.class)
            .extracting(e -> ((HttpClientResponseException)e).getStatus())
            .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        URI deleteUri =UriBuilder.of("/question").path(QUESTION_ID).path("answer").path(answerId).path("delete").build();

        assertThat(client.exchange(BrowserRequest.POST(deleteUri.toString(), Collections.emptyMap())))
            .matches(redirection(listUri.toString()));

        assertThat(answerRepository.findByQuestionId("xxx", null)).isEmpty();
    }

    @Requires(property = "spec.name", value = "AnswerControllerFormTest")
    @Singleton
    @Replaces(QuestionRepository.class)
    static class QuestionRepositoryMock implements QuestionRepository {

        private final IdGenerator idGenerator;

        QuestionRepositoryMock(IdGenerator idGenerator) {
            this.idGenerator = idGenerator;
        }

        @Override
        public String save(QuestionSave questionSave, Tenant tenant) {
            return idGenerator.generate();
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
            return Collections.emptyList();
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

    @Requires(property = "spec.name", value = "AnswerControllerFormTest")
    @Singleton
    @Replaces(AnswerRepository.class)
    static class AnswerRepositoryMock implements AnswerRepository {

        Map<String, Answer> answers = new HashMap<>();

        @NonNull
        public Optional<AnswerUpdate> findAnswerUpdate(@NotBlank String questionId,
                                                       @NotBlank String id,
                                                       @Nullable Tenant tenant) {
            return Optional.empty();
        }

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
            answers.put(id, new Answer(id, "xxx", new Element("zzz", "user"), answerSave.answerDate(), answerSave.text()));
            return id;
        }

        @Override
        public void update(AnswerUpdate answerUpdate, @Nullable Tenant tenant) {
            final String id = answerUpdate.id();
            if (answers.containsKey(id)) {
                answers.put(id, new Answer(id, "xxx",  new Element("zzz", "user"), answerUpdate.answerDate(), answerUpdate.text()));
            }
        }

        @Override
        public void deleteById(String id, @Nullable Tenant tenant) {
            answers.remove(id);
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
