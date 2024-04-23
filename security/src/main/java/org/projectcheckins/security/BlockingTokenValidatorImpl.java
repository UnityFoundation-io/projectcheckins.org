package org.projectcheckins.security;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.validator.TokenValidator;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static io.micronaut.scheduling.TaskExecutors.BLOCKING;

@Singleton
public class BlockingTokenValidatorImpl implements BlockingTokenValidator {

    private static final Logger LOG = LoggerFactory.getLogger(BlockingTokenValidatorImpl.class);

    private final TokenValidator<?> tokenValidator;
    private final ExecutorService executor;

    public BlockingTokenValidatorImpl(TokenValidator<?> tokenValidator, @Named(BLOCKING) ExecutorService executor) {
        this.tokenValidator = tokenValidator;
        this.executor = executor;
    }

    @Override
    public Optional<Authentication> validateToken(String token) {
        try {
            final Publisher<Authentication> result = tokenValidator.validateToken(token, null);
            final Callable<Optional<Authentication>> task = Mono.from(result)::blockOptional;
            return executor.submit(task).get();
        } catch (ExecutionException | InterruptedException e) {
            LOG.warn("Could not validate token {}", token, e);
            return Optional.empty();
        }
    }
}
