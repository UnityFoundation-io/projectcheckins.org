package org.projectcheckins.http.controllers;

import com.softamo.concertados.annotations.GetHtml;
import com.softamo.concertados.annotations.PostForm;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.fields.FormGenerator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.forms.Question;
import org.projectcheckins.core.forms.QuestionSave;
import org.projectcheckins.core.forms.QuestionUpdate;
import org.projectcheckins.core.repositories.QuestionRepository;

import java.net.URI;
import java.util.Map;
import java.util.function.Function;

@Controller
class QuestionController {

    private static final String QUESTION = "question";
    private static final String PATH = ApiConstants.SLASH + QUESTION;

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
    private static final Function<String, URI> PATH_SHOW_BUILDER  = id -> UriBuilder.of(PATH).path(id).path(ApiConstants.ACTION_SHOW).build();
    private static final String VIEW_SHOW = PATH + ApiConstants.VIEW_EDIT;

    // EDIT
    private static final String PATH_EDIT = PATH + ApiConstants.PATH_EDIT;
    private static final String VIEW_EDIT = PATH + ApiConstants.VIEW_EDIT;

    // UPDATE
    private static final String PATH_UPDATE = PATH + ApiConstants.PATH_UPDATE;
    private static final Function<String, URI> PATH_UPDATE_BUILDER  = id -> UriBuilder.of(PATH).path(id).path(ApiConstants.ACTION_UPDATE).build();

    // DELETE
    private static final String PATH_DELETE = PATH + ApiConstants.PATH_DELETE;

    private final FormGenerator formGenerator;
    private final QuestionRepository questionRepository;

    QuestionController(FormGenerator formGenerator,
                       QuestionRepository questionRepository) {
        this.formGenerator = formGenerator;
        this.questionRepository = questionRepository;
    }

    @GetHtml(uri = PATH_LIST, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_LIST)
    Map<String, Object> questionList() {
        return Map.of(MODEL_QUESTIONS, questionRepository.findAll());
    }

    @GetHtml(uri = PATH_CREATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_CREATE)
    Map<String, Object> questionCreate() {
        return Map.of(ApiConstants.MODEL_FORM, formGenerator.generate(PATH_SAVE, QuestionSave.class));
    }
    @PostForm(uri = PATH_SAVE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionSave(@NonNull @NotNull @Valid @Body QuestionSave questionSave) {
        String id = questionRepository.save(questionSave);
        return HttpResponse.seeOther(PATH_SHOW_BUILDER.apply(id));
    }

    @GetHtml(uri = PATH_SHOW, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_SHOW)
    HttpResponse<?> questionShow(@PathVariable @NotBlank String id) {
        return questionRepository.findById(id)
                .map(question -> (HttpResponse) HttpResponse.ok(Map.of(MODEL_QUESTION, question)))
                .orElseGet(NotFoundController.NOT_FOUND_REDIRECT);
    }

    @GetHtml(uri = PATH_EDIT, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_EDIT)
    HttpResponse<?> questionEdit(@PathVariable @NotBlank String id) {
        return questionRepository.findById(id)
                .map(question -> (HttpResponse) HttpResponse.ok(updateModel(question)))
                .orElseGet(NotFoundController.NOT_FOUND_REDIRECT);
    }

    @PostForm(uri = PATH_UPDATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionUpdate(@PathVariable @NotBlank String id,
                                   @NonNull @NotNull @Valid @Body QuestionUpdate questionUpdate) {
        if (!id.equals(questionUpdate.id())) {
            return HttpResponse.unprocessableEntity();
        }
        questionRepository.update(questionUpdate);
        return HttpResponse.seeOther(PATH_SHOW_BUILDER.apply(id));
    }

    @PostForm(uri = PATH_DELETE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> questionDelete(@PathVariable @NotBlank String id) {
        return HttpResponse.seeOther(URI.create(PATH_LIST));
    }

    @NonNull
    private Map<String, Object> updateModel(@NonNull Question question) {
        return Map.of(ApiConstants.MODEL_FORM, formGenerator.generate(PATH_UPDATE_BUILDER.apply(question.id()).toString(), new QuestionUpdate(question.id(), question.title())));
    }

}
