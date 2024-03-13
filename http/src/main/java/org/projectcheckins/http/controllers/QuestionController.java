package org.projectcheckins.http.controllers;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.fields.Form;
import io.micronaut.views.fields.messages.Message;
import io.swagger.v3.oas.annotations.Hidden;
import org.projectcheckins.annotations.GetHtml;
import org.projectcheckins.annotations.PostForm;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.fields.FormGenerator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.bootstrap.Breadcrumb;
import org.projectcheckins.core.forms.*;
import org.projectcheckins.core.repositories.AnswerRepository;
import org.projectcheckins.core.repositories.ProfileRepository;
import org.projectcheckins.core.repositories.QuestionRepository;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@Controller
class QuestionController {

    public static final String QUESTION = "question";
    public static final String PATH = ApiConstants.SLASH + QUESTION;

    private static final String MODEL_QUESTIONS = "questions";
    public static final String MODEL_QUESTION = "question";

    // LIST
    public static final String PATH_LIST = PATH + ApiConstants.PATH_LIST;
    private static final String VIEW_LIST = PATH + ApiConstants.VIEW_LIST;
    public static final Breadcrumb BREADCRUMB_QUESTION_LIST = new Breadcrumb(Message.of("Questions", "question.list"), PATH_LIST);

    // CREATE
    private static final String PATH_CREATE = PATH + ApiConstants.PATH_CREATE;
    private static final String VIEW_CREATE = PATH + ApiConstants.VIEW_CREATE;

    public static final List<Breadcrumb> BREADCRUMBS_QUESTION_CREATE = List.of(BREADCRUMB_QUESTION_LIST, ApiConstants.BREADCRUMB_CREATE);

    // SAVE
    private static final String PATH_SAVE = PATH + ApiConstants.PATH_SAVE;

    // SHOW
    private static final String MODEL_ANSWERS = "answers";
    private static final String PATH_SHOW = PATH + ApiConstants.PATH_SHOW;
    private static final Function<String, URI> PATH_SHOW_BUILDER  = id -> UriBuilder.of(PATH).path(id).path(ApiConstants.ACTION_SHOW).build();
    private static final String VIEW_SHOW = PATH + ApiConstants.VIEW_SHOW;
    public static final BiFunction<String, String, Breadcrumb> BREADCRUMB_QUESTION_SHOW = (id, name) -> new Breadcrumb(Message.of(name), PATH_SHOW_BUILDER.apply(id).toString());

    public static final Function<String, List<Breadcrumb>> BREADCRUMBS_QUESTION_SHOW = (name) -> List.of(BREADCRUMB_QUESTION_LIST, new Breadcrumb(Message.of(name), null));

    // EDIT
    private static final String PATH_EDIT = PATH + ApiConstants.PATH_EDIT;
    private static final String VIEW_EDIT = PATH + ApiConstants.VIEW_EDIT;

    public static final BiFunction<String, String, List<Breadcrumb>> BREADCRUMBS_QUESTION_EDIT = (id, name) -> List.of(BREADCRUMB_QUESTION_LIST, BREADCRUMB_QUESTION_SHOW.apply(id, name), ApiConstants.BREADCRUMB_EDIT);


    // UPDATE
    private static final String PATH_UPDATE = PATH + ApiConstants.PATH_UPDATE;
    private static final Function<String, URI> PATH_UPDATE_BUILDER  = id -> UriBuilder.of(PATH).path(id).path(ApiConstants.ACTION_UPDATE).build();

    // DELETE
    private static final String PATH_DELETE = PATH + ApiConstants.PATH_DELETE;

    private final FormGenerator formGenerator;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ProfileRepository profileRepository;

    QuestionController(FormGenerator formGenerator,
                       QuestionRepository questionRepository,
                       AnswerRepository answerRepository,
                       ProfileRepository profileRepository) {
        this.formGenerator = formGenerator;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.profileRepository = profileRepository;
    }

    @GetHtml(uri = PATH_LIST, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_LIST)
    Map<String, Object> questionList(@Nullable Tenant tenant) {
        return Map.of(MODEL_QUESTIONS, questionRepository.findAll(tenant));
    }

    @GetHtml(uri = PATH_CREATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_CREATE)
    Map<String, Object> questionCreate() {
        return Map.of(ApiConstants.MODEL_BREADCRUMBS, BREADCRUMBS_QUESTION_CREATE,
                ApiConstants.MODEL_FORM, formGenerator.generate(PATH_SAVE, QuestionSave.class));
    }
    @PostForm(uri = PATH_SAVE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionSave(@NonNull @NotNull @Valid @Body QuestionSave questionSave,
                                 @Nullable Tenant tenant) {
        String id = questionRepository.save(questionSave, tenant);
        return HttpResponse.seeOther(PATH_SHOW_BUILDER.apply(id));
    }

    @GetHtml(uri = PATH_SHOW, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_SHOW)
    HttpResponse<?> questionShow(@PathVariable @NotBlank String id,
                                 @NonNull Authentication authentication,
                                 @Nullable Tenant tenant) {
        return questionRepository.findById(id, tenant)
                .map(question -> HttpResponse.ok(Map.of(MODEL_QUESTION, question)))
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
                                   @NonNull @NotNull @Valid @Body QuestionUpdate questionUpdate,
                                   @Nullable Tenant tenant) {
        if (!id.equals(questionUpdate.id())) {
            return HttpResponse.unprocessableEntity();
        }
        questionRepository.update(questionUpdate, tenant);
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
        Form form = formGenerator.generate(PATH_UPDATE_BUILDER.apply(question.id()).toString(), new QuestionUpdate(
                question.id(),
                question.title(),
                question.schedule()));
        return Map.of(ApiConstants.MODEL_BREADCRUMBS, BREADCRUMBS_QUESTION_EDIT.apply(question.id(), question.title()),
                ApiConstants.MODEL_FORM, form);
    }

    @NonNull
    private Map<String, Object> showModel(@NonNull Question question,
                                          @NonNull Authentication authentication,
                                          @Nullable Tenant tenant) {
        Format format = profileRepository.findPreferedFormat(authentication);
        LocalDate answerDate = LocalDate.now();
        Map<String, Object> model = new HashMap<>();
        model.put(ApiConstants.MODEL_BREADCRUMBS, BREADCRUMBS_QUESTION_SHOW.apply(question.title()));
        model.put(MODEL_QUESTION, question);
        model.put(MODEL_ANSWERS, answerRepository.findByQuestionId(question.id(), tenant));
        if (!answerRepository.hasAnswered(question.id(), answerDate, authentication, tenant)) {
            final AnswerSave answerSave = new AnswerSave(question.id(), format, answerDate, "");
            model.put(ApiConstants.MODEL_FORM, formGenerator.generate(AnswerController.ANSWER_PATH_SAVE_BUILDER.apply(question.id()), answerSave));
        }
        return model;
    }
}
