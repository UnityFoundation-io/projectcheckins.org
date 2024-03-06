package org.projectcheckins.http.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.projectcheckins.test.AssertUtils.htmlPage;
import static org.projectcheckins.test.AssertUtils.htmlBody;
import static org.projectcheckins.test.AssertUtils.redirection;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.filters.AuthenticationFetcher;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.*;
import org.projectcheckins.core.models.Element;
import org.projectcheckins.core.repositories.AnswerRepository;
import org.projectcheckins.core.repositories.QuestionRepository;
import org.projectcheckins.test.AbstractAuthenticationFetcher;
import org.projectcheckins.test.BrowserRequest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@Property(name = "spec.name", value = "QuestionControllerTest")
@MicronautTest
class QuestionControllerTest {
    @Test
    void crud(@Client("/") HttpClient httpClient, AuthenticationFetcherMock authenticationFetcherMock) {
        authenticationFetcherMock.setAuthentication(Authentication.build("xxx", Collections.emptyList(), Collections.singletonMap("email", "delamos@unityfoundation.io")));
        BlockingHttpClient client = httpClient.toBlocking();
        assertThat(client.exchange(BrowserRequest.GET("/question/list"), String.class))
            .matches(htmlPage());

        assertThat(client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("xxx").path("edit").build()), String.class))
            .matches(htmlPage());

        assertThat(client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("yyy").path("edit").build()), String.class))
            .matches(redirection("/notFound"));

        assertThat(client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("xxx").path("show").build()), String.class))
            .matches(htmlPage());

        assertThat(client.exchange(BrowserRequest.GET(UriBuilder.of("/question").path("create").build()), String.class))
            .matches(htmlPage())
            .matches(htmlBody("""
                <input type="text" name="title"""));
    }

    @Requires(property = "spec.name", value = "QuestionControllerTest")
    @Singleton
    static class AuthenticationFetcherMock extends AbstractAuthenticationFetcher {
    }

    @Requires(property = "spec.name", value = "QuestionControllerTest")
    @Singleton
    @Replaces(AnswerRepository.class)
    static class AnswerRepositoryMock implements AnswerRepository {

        @Override
        public Optional<Answer> findById(String answerId, @jakarta.annotation.Nullable Tenant tenant) {
            return Optional.empty();
        }

        @Override
        public List<Answer> findByQuestionId(String questionId, @jakarta.annotation.Nullable Tenant tenant) {
            return null;
        }

        @Override
        public String save(AnswerSave answerSave, Authentication authentication, @jakarta.annotation.Nullable Tenant tenant) {
            return null;
        }

        @Override
        public void update(AnswerUpdate answerUpdate, @jakarta.annotation.Nullable Tenant tenant) {

        }

        @Override
        public void deleteById(String id, @jakarta.annotation.Nullable Tenant tenant) {

        }

        @Override
        public Optional<AnswerUpdate> findAnswerUpdate(String questionId, String id, @jakarta.annotation.Nullable Tenant tenant) {
            return Optional.empty();
        }

        @Override
        public boolean hasAnswered(String questionId, LocalDate answerDate, Authentication authentication, @jakarta.annotation.Nullable Tenant tenant) {
            return false;
        }
    }
    @Requires(property = "spec.name", value = "QuestionControllerTest")
    @Singleton
    @Replaces(QuestionRepository.class)
    static class QuestionRepositoryMock implements QuestionRepository {
        @Override
        @NonNull
        public String save(@NotNull @Valid QuestionSave questionSave, @Nullable Tenant tenant) {
            return "xxx";
        }

        @Override
        @NonNull
        public Optional<Question> findById(@NotBlank String id, @Nullable Tenant tenant) {
            if (id.equals("xxx")) {
                return Optional.of(new Question("xxx", "What are working on?"));
            }
            return Optional.empty();
        }

        @Override
        public void update(@NotNull @Valid QuestionUpdate questionUpdate, @Nullable Tenant tenant) {

        }

        @Override
        @NonNull
        public List<Question> findAll(@Nullable Tenant tenant) {
            return Collections.emptyList();
        }

        @Override
        public void deleteById(@NotBlank String id, @Nullable Tenant tenant) {

        }

        @Override
        @NonNull
        public Optional<Element> findElementById(@NotBlank String id, @Nullable Tenant tenant) {
            return Optional.empty();
        }
    }

    @Requires(property = "spec.name", value = "QuestionControllerTest")
    @Singleton
    @Replaces(ProfileRepository.class)
    static class ProfileRepositoryMock implements ProfileRepository {

        @Override
        public Format findPreferedFormat(Authentication authentication) {
            return Format.MARKDOWN;
        }
    }
}
