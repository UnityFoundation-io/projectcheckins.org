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
import io.micronaut.views.fields.messages.Message;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import org.projectcheckins.annotations.GetHtml;
import org.projectcheckins.annotations.PostForm;
import org.projectcheckins.bootstrap.Breadcrumb;
import org.projectcheckins.core.forms.Answer;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.forms.AnswerUpdate;
import org.projectcheckins.core.models.Element;
import org.projectcheckins.core.repositories.AnswerRepository;
import org.projectcheckins.core.repositories.QuestionRepository;

import java.net.URI;
import java.util.function.Function;

import static org.projectcheckins.http.controllers.ApiConstants.MODEL_BREADCRUMBS;

@Controller
class AnswerController {
    private static final String ANSWER = "answer";
    private static final String PATH = QuestionController.PATH + ApiConstants.SLASH + "{questionId}" + ApiConstants.SLASH + ANSWER;

    private static final String MODEL_ANSWER = "answer";

    // LIST
    private static final String PATH_LIST = PATH + ApiConstants.PATH_LIST;
    private static final String VIEW_LIST = ApiConstants.SLASH + ANSWER + ApiConstants.VIEW_LIST;
    public static final Breadcrumb BREADCRUMB_ANSWER_LIST = new Breadcrumb(Message.of("Answers", "answer.list"));

    // CREATE
    private static final String PATH_CREATE = PATH + ApiConstants.PATH_CREATE;
    private static final String VIEW_CREATE = ApiConstants.SLASH + ANSWER + ApiConstants.VIEW_CREATE;

    private static final Function<String, URI> PATH_LIST_BUILDER = questionId -> UriBuilder
        .of(QuestionController.PATH).path(questionId).path(ANSWER).path(ApiConstants.ACTION_LIST).build();

    // SAVE
    private static final String PATH_SAVE = PATH + ApiConstants.PATH_SAVE;

    public static final Function<String, String> ANSWER_PATH_SAVE_BUILDER  = questionId -> new StringBuilder()
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
    private final QuestionRepository questionRepository;

    AnswerController(FormGenerator formGenerator,
                     AnswerRepository answerRepository,
                     QuestionRepository questionRepository) {
        this.formGenerator = formGenerator;
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    @Hidden
    @GetHtml(uri = PATH_LIST, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_LIST)
    HttpResponse<?> answerList(@NotBlank @PathVariable String questionId, @Nullable Tenant tenant) {
        Optional<Element> questionOptional = questionRepository.findElementById(questionId, tenant);
        if (questionOptional.isEmpty()) {
            return HttpResponse.notFound();
        }
        Element question = questionOptional.get();
        return HttpResponse.ok(answerListModel(question, tenant));
    }

    @NonNull
    private Map<String, Object> answerListModel(@NonNull Element question, @Nullable Tenant tenant) {
        return Map.of(MODEL_BREADCRUMBS,
                List.of(QuestionController.BREADCRUMB_QUESTION_LIST,
                        QuestionController.BREADCRUMB_QUESTION_SHOW.apply(question.id(), question.name()),
                        BREADCRUMB_ANSWER_LIST),
                QuestionController.MODEL_QUESTION, question);
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
        Optional<Element> questionOptional = questionRepository.findElementById(questionId, tenant);
        if (questionOptional.isEmpty()) {
            return HttpResponse.notFound();
        }
        Element element = questionOptional.get();
        return answerRepository.findById(id, tenant)
            .map(answer -> (HttpResponse) HttpResponse.ok(Map.of(QuestionController.MODEL_QUESTION, element, MODEL_ANSWER, answer)))
            .orElseGet(NotFoundController::notFoundRedirect);
    }

    @Hidden
    @Produces(MediaType.TEXT_HTML)
    @Get(PATH_EDIT)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> answerEdit(@PathVariable @NotBlank String questionId,
                               @PathVariable @NotBlank String id,
                               @Nullable Tenant tenant) {
        Optional<Element> questionOptional = questionRepository.findElementById(questionId, tenant);
        if (questionOptional.isEmpty()) {
            return HttpResponse.notFound();
        }
        Element element = questionOptional.get();
        return answerRepository.findAnswerUpdate(questionId, id, tenant)
                .map(answerUpdate -> (HttpResponse) HttpResponse.ok(new ModelAndView<>(VIEW_EDIT, updateModel(element, answerUpdate))))
                .orElseGet(NotFoundController::notFoundRedirect);
    }

    @PostForm(uri = PATH_UPDATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionUpdate(@PathVariable @NotBlank String questionId,
                                   @PathVariable @NotBlank String id,
                                   @NonNull @NotNull @Valid @Body AnswerUpdate answerUpdate,
                                   @Nullable Tenant tenant) {
        if (!id.equals(answerUpdate.id())) {
            return HttpResponse.unprocessableEntity();
        }
        if (!questionId.equals(answerUpdate.questionId())) {
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
    private Map<String, Object> updateModel(@NonNull Element element, @NonNull AnswerUpdate answerUpdate) {
        Form form = formGenerator.generate(PATH_SHOW_BUILDER.apply(answerUpdate.questionId(), answerUpdate.id()).toString(), answerUpdate);
        return Map.of(ApiConstants.MODEL_FORM, form, QuestionController.MODEL_QUESTION, element);
    }
}
