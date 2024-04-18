package org.projectcheckins.http.controllers;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.fields.Form;
import io.micronaut.views.fields.FormGenerator;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.annotations.GetHtml;
import org.projectcheckins.annotations.PostForm;
import org.projectcheckins.bootstrap.Breadcrumb;
import org.projectcheckins.core.api.Answer;
import org.projectcheckins.core.api.AnswerView;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.forms.AnswerForm;
import org.projectcheckins.core.forms.AnswerMarkdownSave;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.forms.AnswerUpdate;
import org.projectcheckins.core.forms.AnswerUpdateMarkdown;
import org.projectcheckins.core.forms.AnswerUpdateRecord;
import org.projectcheckins.core.forms.AnswerUpdateWysiwyg;
import org.projectcheckins.core.forms.AnswerWysiwygSave;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.services.AnswerService;
import org.projectcheckins.core.services.QuestionService;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
class AnswerController {
    private static final String ANSWER = "answer";
    private static final String MODEL_ANSWER = "answer";
    private static final String PATH = QuestionController.PATH + ApiConstants.SLASH + "{questionId}" + ApiConstants.SLASH + ANSWER;

    // Answer save WYSIWYG
    private static final String MARKDOWN = "markdown";

    // Answer save markdown
    private static final String WYSIWYG = "wysiwyg";

    // SAVE
    private static final String PATH_ANSWER_SAVE_MARKDOWN = QuestionController.PATH + ApiConstants.SLASH + "{questionId}" + ApiConstants.SLASH + ANSWER + ApiConstants.SLASH + MARKDOWN;
    private static final String REGEX_SAVE_MARKDOWN = "^\\/question\\/.*\\/answer/markdown$";
    private static final Function<String, URI> URI_BUILDER_ANSWER_SAVE_MARKDOWN = questionId ->
            UriBuilder.of(QuestionController.PATH).path(questionId).path(ANSWER).path(MARKDOWN).build();

    private static final String PATH_ANSWER_SAVE_WYSIWYG = QuestionController.PATH + ApiConstants.SLASH + "{questionId}" + ApiConstants.SLASH + ANSWER + ApiConstants.SLASH + WYSIWYG;
    private static final String REGEX_SAVE_WYSIWYG = "^\\/question\\/.*\\/answer/wysiwyg$";
    private static final Function<String, URI> URI_BUILDER_ANSWER_SAVE_WYSIWYG = questionId ->
            UriBuilder.of(QuestionController.PATH).path(questionId).path(ANSWER).path(WYSIWYG).build();

    public static final BiFunction<String, Format, URI> URI_BUILDER_ANSWER_SAVE = (questionId, format) -> switch (format) {
        case MARKDOWN -> URI_BUILDER_ANSWER_SAVE_MARKDOWN.apply(questionId);
        case WYSIWYG -> URI_BUILDER_ANSWER_SAVE_WYSIWYG.apply(questionId);
    };

    // UPDATE
    private static final String PATH_ANSWER_UPDATE = PATH + ApiConstants.PATH_UPDATE;
    private static final Pattern REGEX_UPDATE = Pattern.compile("^/question/(.*)/answer/(.*)/update$");
    private static final Function<Answer, URI> URI_BUILDER_ANSWER_UPDATE = answer -> UriBuilder.of(QuestionController.PATH).path(answer.questionId()).path(ANSWER).path(answer.id()).path(ApiConstants.ACTION_UPDATE).build();

    // SHOW
    private static final String PATH_SHOW = PATH + ApiConstants.PATH_SHOW;
    public static final BiFunction<String, String, URI> PATH_SHOW_URI_BUILDER  = (questionId, id) -> UriBuilder.of(QuestionController.PATH).path(questionId).path(ANSWER).path(id).path(ApiConstants.ACTION_SHOW).build();
    public static final Function<Answer, URI> PATH_SHOW_BUILDER  = answer -> PATH_SHOW_URI_BUILDER.apply(answer.questionId(), answer.id());
    private static final String VIEW_SHOW = ANSWER + ApiConstants.VIEW_SHOW;

    // EDIT
    private static final String PATH_EDIT = PATH + ApiConstants.PATH_EDIT;
    private static final String VIEW_EDIT = ANSWER + ApiConstants.VIEW_EDIT;
    private static final Breadcrumb BREADCRUMB_EDIT = new Breadcrumb(Message.of("Edit Answer", ANSWER + ApiConstants.DOT + ApiConstants.ACTION_EDIT));

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final FormGenerator formGenerator;

    AnswerController(QuestionService questionService, AnswerService answerService, FormGenerator formGenerator) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.formGenerator = formGenerator;
    }

    @PostForm(uri = PATH_ANSWER_SAVE_MARKDOWN, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> answerSaveMarkdown(@PathVariable String questionId,
                                       @NonNull Authentication authentication,
                                       @Nullable Tenant tenant,
                                       @Body @NonNull @NotNull @Valid AnswerMarkdownSave form) {
        if (!questionId.equals(form.questionId())) {
            return HttpResponse.unprocessableEntity();
        }
        if (!authentication.getName().equals(form.respondentId())) {
            return HttpResponse.unprocessableEntity();
        }
        answerService.save(authentication, new AnswerSave(form.questionId(), form.answerDate(), Format.MARKDOWN, form.markdown()), tenant);
        return HttpResponse.seeOther(QuestionController.PATH_SHOW_BUILDER.apply(questionId));
    }

    @PostForm(uri = PATH_ANSWER_SAVE_WYSIWYG, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> answerSaveWysiwyg(@PathVariable String questionId,
                                      @NonNull Authentication authentication,
                                      @Nullable Tenant tenant,
                                      @Body @NonNull @NotNull @Valid AnswerWysiwygSave form) {
        if (!questionId.equals(form.questionId())) {
            return HttpResponse.unprocessableEntity();
        }
        if (!authentication.getName().equals(form.respondentId())) {
            return HttpResponse.unprocessableEntity();
        }
        answerService.save(authentication, new AnswerSave(form.questionId(), form.answerDate(), Format.WYSIWYG, form.html()), tenant);
        return HttpResponse.seeOther(QuestionController.PATH_SHOW_BUILDER.apply(questionId));
    }

    @GetHtml(uri = PATH_SHOW, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_SHOW)
    HttpResponse<?> answerShow(@PathVariable @NotBlank String questionId,
                               @PathVariable @NotBlank String id,
                               @NonNull Authentication authentication,
                               @Nullable Locale locale,
                               @Nullable Tenant tenant) {
        return questionService.findById(questionId, tenant)
                .flatMap(question -> answerService.findById(id, authentication, tenant)
                            .map(answer -> HttpResponse.ok(Map.of(
                                    MODEL_ANSWER, answer,
                                    ApiConstants.MODEL_BREADCRUMBS, List.of(
                                            QuestionController.BREADCRUMB_LIST,
                                            QuestionController.BREADCRUMB_SHOW.apply(question),
                                            makeBreadcrumbShow(answer, locale))
                                    ))))
                .orElseGet(NotFoundController::notFoundRedirect);
    }

    @GetHtml(uri = PATH_EDIT, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_EDIT)
    HttpResponse<?> answerEdit(@PathVariable @NotBlank String questionId,
                               @PathVariable @NotBlank String id,
                               @NonNull Authentication authentication,
                               @Nullable Locale locale,
                               @Nullable Tenant tenant) {
        return questionService.findById(questionId, tenant)
                .flatMap(question -> answerService.findById(id, authentication, tenant)
                        .map(answer -> updateAnswerModel(question, answer, locale)))
                .map(model -> HttpResponse.ok(new ModelAndView<>(VIEW_EDIT, model)))
                .orElseGet(NotFoundController::notFoundRedirect);
    }

    @PostForm(uri = PATH_ANSWER_UPDATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> answerUpdate(@PathVariable String questionId,
                                 @PathVariable String id,
                                 @NonNull Authentication authentication,
                                 @Nullable Tenant tenant,
                                 @Body @NonNull @NotNull @Valid AnswerUpdateRecord answerUpdate) {
        answerService.update(authentication, questionId, id, answerUpdate, tenant);
        return HttpResponse.seeOther(PATH_SHOW_URI_BUILDER.apply(questionId, id));
    }

    @Error(exception = ConstraintViolationException.class)
    public HttpResponse<?> onConstraintViolationException(HttpRequest<?> request,
                                                          @NotNull Authentication authentication,
                                                          @Nullable Locale locale,
                                                          @Nullable Tenant tenant,
                                                          ConstraintViolationException ex) {
        return retrySave(request, authentication, tenant, ex)
                .or(() -> retryUpdate(request, authentication, locale, tenant, ex))
                .orElseGet(HttpResponse::serverError);
    }

    private Optional<HttpResponse<?>> retrySave(HttpRequest<?> request, Authentication auth, Tenant tenant, ConstraintViolationException ex) {
        final Optional<? extends AnswerForm> answerForm;
        if (request.getPath().matches(REGEX_SAVE_MARKDOWN)) {
            answerForm = request.getBody(AnswerMarkdownSave.class);
        } else if (request.getPath().matches(REGEX_SAVE_WYSIWYG)) {
            answerForm = request.getBody(AnswerWysiwygSave.class);
        } else {
            answerForm = Optional.empty();
        }
        return answerForm.map(f -> questionService.findById(f.questionId(), tenant)
                .map(q -> HttpResponse.unprocessableEntity().body(new ModelAndView<>(QuestionController.VIEW_SHOW,
                        QuestionController.showModel(answerService, q, generateForm(f, ex), auth, tenant))))
                .orElseGet(NotFoundController::notFoundRedirect));
    }

    private Optional<HttpResponse<?>> retryUpdate(HttpRequest<?> request, Authentication auth, Locale locale, Tenant tenant, ConstraintViolationException ex) {
        final Matcher update = REGEX_UPDATE.matcher(request.getPath());
        if (!update.find()) {
            return Optional.empty();
        }
        final String questionId = update.group(1);
        final String id = update.group(2);
        return request.getBody(AnswerUpdateRecord.class)
                .map(answerUpdate -> retryUpdate(questionId, id, answerUpdate, auth, locale, tenant, ex));
    }

    private HttpResponse<?> retryUpdate(String questionId, String id, AnswerUpdate answerUpdate, Authentication auth, Locale locale, Tenant tenant, ConstraintViolationException ex) {
        return questionService
                .findById(questionId, tenant)
                .flatMap(question -> answerService.findById(id, auth, tenant)
                        .map(answer -> updateAnswerModel(question, answer, answerUpdate, locale, ex)))
                .map(model -> HttpResponse.ok(new ModelAndView<>(VIEW_EDIT, model)))
                .orElseGet(NotFoundController::notFoundRedirect);
    }

    private Breadcrumb makeBreadcrumbShow(AnswerView view, Locale locale) {
        return new Breadcrumb(Message.of(answerService.getAnswerSummary(view, locale)), PATH_SHOW_BUILDER.andThen(URI::toString).apply(view.answer()));
    }

    @NonNull
    private Map<String, Object> updateAnswerModel(Question question, AnswerView view, Locale locale) {
        final Answer answer = view.answer();
        final AnswerUpdate answerUpdate = switch (answer.format()) {
            case MARKDOWN -> new AnswerUpdateMarkdown(answer.answerDate(), answer.text());
            default -> new AnswerUpdateWysiwyg(answer.answerDate(), answer.text());
        };
        final Form form = formGenerator.generate(URI_BUILDER_ANSWER_UPDATE.apply(answer).toString(), answerUpdate);
        return updateAnswerModel(question, view, form, locale);
    }

    @NonNull
    private Map<String, Object> updateAnswerModel(Question question, AnswerView view, AnswerUpdate record, Locale locale, ConstraintViolationException ex) {
        final AnswerUpdate answerUpdate = switch (record.format()) {
            case MARKDOWN -> new AnswerUpdateMarkdown(record.answerDate(), record.content());
            default -> new AnswerUpdateWysiwyg(record.answerDate(), record.content());
        };
        final Form form = formGenerator.generate(URI_BUILDER_ANSWER_UPDATE.apply(view.answer()).toString(), answerUpdate, ex);
        return updateAnswerModel(question, view, form, locale);
    }

    @NonNull
    private Map<String, Object> updateAnswerModel(Question question, AnswerView view, Form form, Locale locale) {
        return Map.of(
                QuestionController.ANSWER_FORM, form,
                ApiConstants.MODEL_BREADCRUMBS, List.of(
                        QuestionController.BREADCRUMB_LIST,
                        QuestionController.BREADCRUMB_SHOW.apply(question),
                        makeBreadcrumbShow(view, locale),
                        BREADCRUMB_EDIT
                )
        );
    }

    private Form generateForm(AnswerForm form, ConstraintViolationException ex) {
        return formGenerator.generate(URI_BUILDER_ANSWER_SAVE.apply(form.questionId(), getFormat(form)).toString(), form, ex);
    }

    private static Format getFormat(AnswerForm form) {
        return switch (form) {
            case AnswerMarkdownSave markdown -> Format.MARKDOWN;
            case AnswerWysiwygSave wysiwyg -> Format.WYSIWYG;
            default -> null;
        };
    }
}
