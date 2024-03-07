package org.projectcheckins.test;

import io.micronaut.http.*;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.cookie.Cookie;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class BrowserRequest {
    private BrowserRequest() {
    }

    public static <T> MutableHttpRequest<T> POST(String uri, T body) {
        Objects.requireNonNull(uri, "Argument [uri] is required");
        return HttpRequestFactory.INSTANCE.post(uri, body)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.TEXT_HTML);
    }

    public static <T> MutableHttpRequest<T> POST(String uri, Map<CharSequence, CharSequence> headers, T body) {
        return POST(uri, body).headers(headers);
    }

    public static MutableHttpRequest<?> GET(String uri) {
        return HttpRequestFactory.INSTANCE.get(uri).accept(MediaType.TEXT_HTML);
    }

    public static MutableHttpRequest<?> GET(String uri, Map<CharSequence, CharSequence> headers) {
        return GET(uri).headers(headers);
    }

    public static MutableHttpRequest<?> GET(URI uri) {
        return GET(uri.toString());
    }

}
