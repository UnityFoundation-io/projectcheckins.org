package org.projectcheckins.http.exceptionshandlers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import io.micronaut.views.ViewsRenderer;
import jakarta.inject.Singleton;
import java.util.Map;
import org.projectcheckins.core.exceptions.AnswerNotFoundException;

@Produces
@Singleton
public class AnswerNotFoundExceptionHandler extends NotFoundExceptionHandler<AnswerNotFoundException> {

    public AnswerNotFoundExceptionHandler(ErrorResponseProcessor<?> errorResponseProcessor,
                                          ViewsRenderer<Map<?, ?>, HttpRequest<?>> viewsRenderer) {
        super(errorResponseProcessor, viewsRenderer);
    }
}