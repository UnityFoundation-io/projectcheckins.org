package org.projectcheckins.http.controllers;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.fields.Form;
import io.swagger.v3.oas.annotations.Hidden;
import org.projectcheckins.annotations.GetHtml;
import org.projectcheckins.annotations.PostForm;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.rules.SecurityRule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.forms.*;
import org.projectcheckins.core.repositories.QuestionRepository;

import java.net.URI;
import java.time.DayOfWeek;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

@Controller
class QuestionController {

    private static final String QUESTION = "question";
    public static final String PATH = ApiConstants.SLASH + QUESTION;

    private static final String MODEL_QUESTIONS = "questions";
    private static final String MODEL_QUESTION = "question";

    // LIST
    public static final String PATH_LIST = PATH + ApiConstants.PATH_LIST;
    private static final String VIEW_LIST = PATH + ApiConstants.VIEW_LIST;

    // CREATE
    private static final String PATH_CREATE = PATH + ApiConstants.PATH_CREATE;
    private static final String VIEW_CREATE = PATH + ApiConstants.VIEW_CREATE;

    // SAVE
    private static final String PATH_SAVE = PATH + ApiConstants.PATH_SAVE;

    // SHOW
    private static final String PATH_SHOW = PATH + ApiConstants.PATH_SHOW;
    public static final Function<String, URI> PATH_SHOW_BUILDER  = id -> UriBuilder.of(PATH).path(id).path(ApiConstants.ACTION_SHOW).build();
    private static final String VIEW_SHOW = PATH + ApiConstants.VIEW_SHOW;

    // EDIT
    private static final String PATH_EDIT = PATH + ApiConstants.PATH_EDIT;
    private static final String VIEW_EDIT = PATH + ApiConstants.VIEW_EDIT;

    // UPDATE
    private static final String PATH_UPDATE = PATH + ApiConstants.PATH_UPDATE;
    private static final Function<String, URI> PATH_UPDATE_BUILDER  = id -> UriBuilder.of(PATH).path(id).path(ApiConstants.ACTION_UPDATE).build();

    // DELETE
    private static final String PATH_DELETE = PATH + ApiConstants.PATH_DELETE;
    private static final String ANSWER_FORM = "answerForm";
    public static final String MODEL_FIELDSET = "fieldset";

    private final QuestionRepository questionRepository;

    private final AnswerSaveFormGenerator answerSaveFormGenerator;

    QuestionController(QuestionRepository questionRepository,
                       AnswerSaveFormGenerator answerSaveFormGenerator) {
        this.questionRepository = questionRepository;
        this.answerSaveFormGenerator = answerSaveFormGenerator;
    }

    @GetHtml(uri = PATH_LIST, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_LIST)
    Map<String, Object> questionList(@Nullable Tenant tenant) {
        return Map.of(MODEL_QUESTIONS, questionRepository.findAll(tenant));
    }

    @GetHtml(uri = PATH_CREATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_CREATE)
    Map<String, Object> questionCreate() {
        return Map.of(MODEL_FIELDSET, new QuestionSaveForm(null));
    }
    @PostForm(uri = PATH_SAVE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionSave(@NonNull @NotNull @Valid @Body QuestionSaveForm form,
                                 @Nullable Tenant tenant) {
        String id = questionRepository.save(form, tenant);
        return HttpResponse.seeOther(PATH_SHOW_BUILDER.apply(id));
    }

    @GetHtml(uri = PATH_SHOW, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_SHOW)
    HttpResponse<?> questionShow(@PathVariable @NotBlank String id,
                                 @NonNull Authentication authentication,
                                 @Nullable Tenant tenant) {
        Form answerFormSave = answerSaveFormGenerator.generate((Function<Format, String>) format -> AnswerController.URI_BUILDER_ANSWER_SAVE.apply(id, format).toString(), authentication);
        return questionRepository.findById(id, tenant)
                .map(question -> HttpResponse.ok(Map.of(
                        MODEL_QUESTION, question,
                        ANSWER_FORM, answerFormSave
                )))
                .orElseGet(NotFoundController::notFoundRedirect);
    }

    @Hidden
    @Produces(MediaType.TEXT_HTML)
    @Get(PATH_EDIT)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionEdit(@PathVariable @NotBlank String id,
                                 @Nullable Tenant tenant) {
        return questionRepository.findById(id, tenant)
                .map(question -> HttpResponse.ok(new ModelAndView<>(VIEW_EDIT, updateModel(question))))
                .orElseGet(NotFoundController::notFoundRedirect);
    }

    @PostForm(uri = PATH_UPDATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionUpdate(@PathVariable @NotBlank String id,
                                   @NonNull @NotNull @Valid @Body QuestionUpdateForm form,
                                   @Nullable Tenant tenant) {
        if (!id.equals(form.id())) {
            return HttpResponse.unprocessableEntity();
        }
        questionRepository.update(form, tenant);
        return HttpResponse.seeOther(PATH_SHOW_BUILDER.apply(id));
    }
 
    @PostForm(uri = PATH_DELETE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionDelete(@PathVariable @NotBlank String id,
                                   @Nullable Tenant tenant) {
        questionRepository.deleteById(id, tenant);
        return HttpResponse.seeOther(URI.create(PATH_LIST));
    }

    @NonNull
    private Map<String, Object> updateModel(@NonNull Question question) {
        QuestionUpdateForm fieldset = new QuestionUpdateForm(question.id(),
                question.title(),
                question.howOften(),
                question.timeOfDay(),
                question.howOften() == HowOften.DAILY_ON ? question.days() : Collections.singleton(DayOfWeek.MONDAY),
                question.howOften() != HowOften.DAILY_ON ? question.days().stream().findFirst().orElse(DayOfWeek.MONDAY) : DayOfWeek.MONDAY);
        return Map.of(MODEL_QUESTION, question, MODEL_FIELDSET, fieldset);
    }

}
