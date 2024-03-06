package org.projectcheckins.test;

import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.filters.AuthenticationFetcher;
import org.reactivestreams.Publisher;

public abstract class AbstractAuthenticationFetcher implements AuthenticationFetcher<HttpRequest<?>> {
    private Authentication authentication;

    @Override
    public Publisher<Authentication> fetchAuthentication(HttpRequest<?> request) {
        return Publishers.just(authentication);
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
}

