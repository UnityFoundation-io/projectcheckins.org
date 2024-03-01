package org.projectcheckins.security.http;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.config.RedirectConfiguration;
import io.micronaut.security.config.RedirectService;
import io.micronaut.security.errors.PriorToLoginPersistence;
import io.micronaut.security.token.cookie.AccessTokenCookieConfiguration;
import io.micronaut.security.token.cookie.RefreshTokenCookieConfiguration;
import io.micronaut.security.token.cookie.TokenCookieLoginHandler;
import io.micronaut.security.token.generator.AccessRefreshTokenGenerator;
import io.micronaut.security.token.generator.AccessTokenConfiguration;
import jakarta.inject.Singleton;

import java.net.URI;

@Singleton
@Replaces(TokenCookieLoginHandler.class)
public class TokenCookieLoginHandlerReplacement extends TokenCookieLoginHandler {
    public TokenCookieLoginHandlerReplacement(RedirectService redirectService,
                                              RedirectConfiguration redirectConfiguration,
                                              AccessTokenCookieConfiguration accessTokenCookieConfiguration,
                                              RefreshTokenCookieConfiguration refreshTokenCookieConfiguration,
                                              AccessTokenConfiguration accessTokenConfiguration,
                                              AccessRefreshTokenGenerator accessRefreshTokenGenerator,
                                              @Nullable PriorToLoginPersistence<HttpRequest<?>, MutableHttpResponse<?>> priorToLoginPersistence) {
        super(redirectService, redirectConfiguration, accessTokenCookieConfiguration, refreshTokenCookieConfiguration, accessTokenConfiguration, accessRefreshTokenGenerator, priorToLoginPersistence);
    }

    @Override
    public MutableHttpResponse<?> loginFailed(AuthenticationResponse authenticationFailed, HttpRequest<?> request) {
            if (loginFailure == null) {
                return HttpResponse.unauthorized();
            }
            UriBuilder uriBuilder = UriBuilder.of(loginFailure);
            if (authenticationFailed instanceof AuthenticationFailed failure && failure.getReason() != null) {
                uriBuilder = uriBuilder.queryParam("reason", failure.getReason());
            }
            URI location = uriBuilder.build();
            return HttpResponse.seeOther(location);
    }
}
