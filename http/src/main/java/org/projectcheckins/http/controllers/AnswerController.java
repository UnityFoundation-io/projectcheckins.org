package org.projectcheckins.http.controllers;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.annotations.PostForm;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.repositories.AnswerRepository;

import java.net.URI;
import java.util.function.Function;

@Controller
class AnswerController {
    private static final String ANSWER = "answer";
    private static final String PATH = QuestionController.PATH + ApiConstants.SLASH + "{questionId}" + ApiConstants.SLASH + ANSWER;

    // LIST
    private static final String PATH_LIST = PATH + ApiConstants.PATH_LIST;
    private static final Function<String, URI> PATH_LIST_BUILDER = id -> UriBuilder.of(QuestionController.PATH)
            .path(id)
            .path(ANSWER)
            .path(ApiConstants.ACTION_LIST).build();

    // SAVE
    private static final String PATH_SAVE = PATH + ApiConstants.PATH_SAVE;

    private final AnswerRepository answerRepository;

    AnswerController(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @PostForm(uri = PATH_SAVE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    @SuppressWarnings("rawtypes")
    HttpResponse answerSave(@NotBlank @PathVariable String questionId,
                               @NotNull @Valid @Body AnswerSave answerSave,
                               @NonNull @NotNull Authentication authentication//,
                               //@Nullable Tenant tenant
    ) {
        if (!answerSave.questionId().equals(questionId)) {
            return HttpResponse.unprocessableEntity();
        }
        answerRepository.save(answerSave, authentication//,
        // tenant
                );
        return HttpResponse.redirect(PATH_LIST_BUILDER.apply(questionId));
    }

}
