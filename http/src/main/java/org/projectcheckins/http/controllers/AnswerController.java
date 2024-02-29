package org.projectcheckins.http.controllers;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.fields.Form;
import io.micronaut.views.fields.FormGenerator;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import org.projectcheckins.annotations.GetHtml;
import org.projectcheckins.annotations.PostForm;
import org.projectcheckins.core.forms.Answer;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.forms.AnswerUpdate;
import org.projectcheckins.core.repositories.AnswerRepository;

import java.net.URI;
import java.util.function.Function;

@Controller
class AnswerController {
    private static final String ANSWER = "answer";
    private static final String PATH = QuestionController.PATH + ApiConstants.SLASH + "{questionId}" + ApiConstants.SLASH + ANSWER;

    private static final String MODEL_ANSWERS = "answers";
    private static final String MODEL_ANSWER = "answer";
    private static final String MODEL_QUESTION_ID = "questionId";

    // LIST
    private static final String PATH_LIST = PATH + ApiConstants.PATH_LIST;
    private static final String VIEW_LIST = ApiConstants.SLASH + ANSWER + ApiConstants.VIEW_LIST;

    // CREATE
    private static final String PATH_CREATE = PATH + ApiConstants.PATH_CREATE;
    private static final String VIEW_CREATE = ApiConstants.SLASH + ANSWER + ApiConstants.VIEW_CREATE;

    private static final Function<String, URI> PATH_LIST_BUILDER = questionId -> UriBuilder
        .of(QuestionController.PATH).path(questionId).path(ANSWER).path(ApiConstants.ACTION_LIST).build();

    // SAVE
    private static final String PATH_SAVE = PATH + ApiConstants.PATH_SAVE;

    private static final Function<String, String> PATH_SAVE_BUILDER  = questionId -> new StringBuilder()
        .append(QuestionController.PATH).append(ApiConstants.SLASH).append(questionId).append(ApiConstants.SLASH).append(ANSWER).append(ApiConstants.SLASH).append(ApiConstants.ACTION_SAVE).toString();

    // SHOW
    private static final String PATH_SHOW = PATH + ApiConstants.PATH_SHOW;
    private static final BiFunction<String, String, URI> PATH_SHOW_BUILDER  = (questionId, answerId) -> UriBuilder
        .of(QuestionController.PATH).path(questionId).path(ANSWER).path(answerId).path(ApiConstants.ACTION_SHOW).build();
    private static final String VIEW_SHOW = ApiConstants.SLASH + ANSWER + ApiConstants.VIEW_SHOW;

    // EDIT
    private static final String PATH_EDIT = PATH + ApiConstants.PATH_EDIT;
    private static final String VIEW_EDIT = ApiConstants.SLASH + ANSWER + ApiConstants.VIEW_EDIT;

    // UPDATE
    private static final String PATH_UPDATE = PATH + ApiConstants.PATH_UPDATE;
    private static final BiFunction<String, String, URI> PATH_UPDATE_BUILDER  = (questionId, answerId) -> UriBuilder
        .of(QuestionController.PATH).path(questionId).path(ANSWER).path(answerId).path(ApiConstants.ACTION_UPDATE).build();

    // DELETE
    private static final String PATH_DELETE = PATH + ApiConstants.PATH_DELETE;

    private final FormGenerator formGenerator;

    private final AnswerRepository answerRepository;

    AnswerController(FormGenerator formGenerator, AnswerRepository answerRepository) {
        this.formGenerator = formGenerator;
        this.answerRepository = answerRepository;
    }

    @Hidden
    @GetHtml(uri = PATH_LIST, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_LIST)
    @SuppressWarnings("rawtypes")
    Map<String, Object> answerList(@NotBlank @PathVariable String questionId, @Nullable Tenant tenant) {
        return Map.of(MODEL_QUESTION_ID, questionId, MODEL_ANSWERS, answerRepository.findByQuestionId(questionId, tenant));
    }

    @GetHtml(uri = PATH_CREATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_CREATE)
    Map<String, Object> answerCreate(@NotBlank @PathVariable String questionId) {
        final AnswerSave answerSave = new AnswerSave(questionId, LocalDate.now(), "");
        return Map.of(ApiConstants.MODEL_FORM, formGenerator.generate(PATH_SAVE_BUILDER.apply(questionId), answerSave));
    }

    @PostForm(uri = PATH_SAVE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    @SuppressWarnings("rawtypes")
    HttpResponse answerSave(@NotBlank @PathVariable String questionId,
                            @NotNull @Valid @Body AnswerSave answerSave,
                            @Nullable Authentication authentication,
                            @Nullable Tenant tenant
    ) {
        if (!answerSave.questionId().equals(questionId)) {
            return HttpResponse.unprocessableEntity();
        }
        answerRepository.save(answerSave, authentication, tenant);
        return HttpResponse.seeOther(PATH_LIST_BUILDER.apply(questionId));
    }

    @GetHtml(uri = PATH_SHOW, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_SHOW)
    HttpResponse<?> answerShow(@NotBlank @PathVariable String questionId, @PathVariable @NotBlank String id, @Nullable Tenant tenant) {
        // TODO: Should we check that question IDs match?
        return answerRepository.findById(id, tenant)
            .map(answer -> (HttpResponse) HttpResponse.ok(Map.of(MODEL_ANSWER, answer)))
            .orElseGet(NotFoundController.NOT_FOUND_REDIRECT);
    }

    @Hidden
    @Produces(MediaType.TEXT_HTML)
    @Get(PATH_EDIT)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> answerEdit(@PathVariable @NotBlank String questionId,
                               @PathVariable @NotBlank String id,
                               @Nullable Tenant tenant) {
        // TODO: Should we check that question IDs match?
        return answerRepository.findById(id, tenant)
            .map(answer -> (HttpResponse) HttpResponse.ok(new ModelAndView<>(VIEW_EDIT, updateModel(questionId, answer))))
            .orElseGet(NotFoundController.NOT_FOUND_REDIRECT);
    }

    @PostForm(uri = PATH_UPDATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionUpdate(@PathVariable @NotBlank String questionId,
                                   @PathVariable @NotBlank String id,
                                   @NonNull @NotNull @Valid @Body AnswerUpdate answerUpdate,
                                   @Nullable Tenant tenant) {
        // TODO: Should we check that question IDs match?
        if (!id.equals(answerUpdate.id())) {
            return HttpResponse.unprocessableEntity();
        }
        answerRepository.update(answerUpdate, tenant);
        return HttpResponse.seeOther(PATH_SHOW_BUILDER.apply(questionId, id));
    }

    @PostForm(uri = PATH_DELETE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> answerDelete(@PathVariable @NotBlank String questionId,
                                 @PathVariable @NotBlank String id,
                                 @Nullable Tenant tenant) {
        // TODO: Should we check that question IDs match?
        answerRepository.deleteById(id, tenant);
        return HttpResponse.seeOther(PATH_LIST_BUILDER.apply(questionId));
    }

    @NonNull
    private Map<String, Object> updateModel(@NotBlank @PathVariable String questionId, @NonNull Answer answer) {
        Form form = formGenerator.generate(PATH_UPDATE_BUILDER.apply(questionId, answer.id()).toString(), new AnswerUpdate(answer.id(), answer.answerDate(), answer.text()));
        return Map.of(ApiConstants.MODEL_FORM, form);
    }
}
