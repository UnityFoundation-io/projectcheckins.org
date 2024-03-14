package org.projectcheckins.http.controllers;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.repositories.SecondaryAnswerRepository;
import org.projectcheckins.test.AbstractAuthenticationFetcher;
import org.projectcheckins.test.BrowserRequest;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
@Property(name = "spec.name", value = "AnswerControllerTest")
@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
class AnswerControllerTest {
    @Test
    void crud(@Client("/") HttpClient httpClient,
              AuthenticationFetcherMock authenticationFetcher,
              AnswerRepositoryMock answerRepositoryMock) {
        BlockingHttpClient client = httpClient.toBlocking();
        authenticationFetcher.setAuthentication(AbstractAuthenticationFetcher.SDELAMO);
        String questionId = "123";

        String markdown = """
                                    - 4.4.0 core release and building every module with 4.4.0 core.
                                    - HTMX integration
                                    - Project checkins
                        """;
        URI markdownUri = UriBuilder.of("/question").path(questionId).path("answer").path("markdown").build();
        Map<String, Object> body = Map.of("questionId", questionId,
                "answerDate", "2024-03-11",
                "markdown", markdown);
        HttpRequest<?> request = BrowserRequest.POST(markdownUri.toString(), body);
        assertDoesNotThrow(() -> client.exchange(request));

        String html = """
                                    <ul>
                                    <li>4.4.0 core release and building every module with 4.4.0 core.</li>
                                    <li>HTMX integration</li>
                                    <li>Project checkins</li>
                                    </ul>
                        """;
        URI htmlUri = UriBuilder.of("/question").path(questionId).path("answer").path("wysiwyg").build();
        Map<String, Object> bodyHtml = Map.of("questionId", questionId,
                "answerDate", "2024-03-11",
                "html", html);
        HttpRequest<?> wysiwygRequest = BrowserRequest.POST(htmlUri.toString(), bodyHtml);
        assertDoesNotThrow(() -> client.exchange(wysiwygRequest));
        LocalDate expectedAnswerDate = LocalDate.of(2024, 3, 11);
        assertThat(answerRepositoryMock.getAnswers())
                .anyMatch(a -> a.format() == Format.MARKDOWN && a.text().equals(markdown) && a.answerDate().equals(expectedAnswerDate))
                .anyMatch(a -> a.format() == Format.WYSIWYG && a.text().equals(html) && a.answerDate().equals(expectedAnswerDate));
    }

    @Requires(property = "spec.name", value = "AnswerControllerTest")
    @Singleton
    static class AuthenticationFetcherMock extends AbstractAuthenticationFetcher {
    }

    @Requires(property = "spec.name", value = "AnswerControllerTest")
    @Singleton
    static class AnswerRepositoryMock extends SecondaryAnswerRepository {

        private List<AnswerSave> answers = new ArrayList<>();
        @Override
        public void save(@NonNull @NotNull Authentication authentication,
                         @NonNull @NotNull @Valid AnswerSave answerSave) {
            this.answers.add(answerSave);
        }

        public List<AnswerSave> getAnswers() {
            return answers;
        }
    }

}