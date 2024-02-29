package org.projectcheckins.http.exceptionshandlers;

import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import io.micronaut.views.ViewsRenderer;
import jakarta.inject.Singleton;
import java.util.Collections;
import java.util.Map;
import org.projectcheckins.core.exceptions.NotFoundException;

@Produces
@Singleton
public class NotFoundExceptionHandler<T extends NotFoundException> implements ExceptionHandler<T, HttpResponse<?>> {

    protected final ErrorResponseProcessor<?> errorResponseProcessor;
    protected final ViewsRenderer<Map<?, ?>, HttpRequest<?>> viewsRenderer;

    public NotFoundExceptionHandler(ErrorResponseProcessor<?> errorResponseProcessor,
                                    ViewsRenderer<Map<?, ?>, HttpRequest<?>> viewsRenderer) {
        this.errorResponseProcessor = errorResponseProcessor;
        this.viewsRenderer = viewsRenderer;
    }

    @Override
    public HttpResponse<?> handle(@SuppressWarnings("rawtypes") HttpRequest request, T e) {
        if (acceptHtml(request)) {
            Writable writable = viewsRenderer.render("error/404", Collections.emptyMap(), request);
            return HttpResponse.notFound(writable)
                    .contentType(MediaType.TEXT_HTML_TYPE);
        }
        return errorResponseProcessor.processResponse(ErrorContext.builder(request)
                .cause(e)
                .errorMessage("Not found")
                .build(), HttpResponse.notFound());
    }

    protected boolean acceptHtml(HttpRequest<?> request) {
        return request.getHeaders()
                .accept()
                .stream()
                .anyMatch(mediaType -> mediaType.getName().contains(MediaType.TEXT_HTML));
    }
}