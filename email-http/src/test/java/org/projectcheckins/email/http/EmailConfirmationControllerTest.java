package org.projectcheckins.email.http;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.projectcheckins.email.EmailConfirmationTokenGenerator;
import org.projectcheckins.security.UserRepository;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@Property(name = "spec.name", value = "EmailConfirmationControllerTest")
@MicronautTest
class EmailConfirmationControllerTest {
    @Test
    void successfulRedirection(@Client("/") HttpClient httpClient,
                               UserRepositoryMock userRepository,
                               EmailConfirmationTokenGenerator tokenGenerator) {
        BlockingHttpClient client = httpClient.toBlocking();
        String email = "delamos@unityfoundation.io";
        String token = tokenGenerator.generateToken(email);
        URI uri = UriBuilder.of("/email").path("confirm").queryParam("token", token).build();
        HttpRequest<?> request = HttpRequest.GET(uri);
        HttpResponse<?> response = client.exchange(request);
        assertEquals("/security/login", response.getHeaders().get(HttpHeaders.LOCATION));
        assertTrue(userRepository.getUserIds().contains(email));

        uri = UriBuilder.of("/email").path("confirm").queryParam("token", "bogus").build();
        request = HttpRequest.GET(uri);
        response = client.exchange(request);
        assertEquals("/", response.getHeaders().get(HttpHeaders.LOCATION));
    }

    @Requires(property = "spec.name", value = "EmailConfirmationControllerTest")
    @Singleton
    static class UserRepositoryMock implements UserRepository {
        List<String> userIds = new ArrayList<>();

        @Override
        public void enable(String userId) {
            userIds.add(userId);
        }

        public List<String> getUserIds() {
            return userIds;
        }
    }
}