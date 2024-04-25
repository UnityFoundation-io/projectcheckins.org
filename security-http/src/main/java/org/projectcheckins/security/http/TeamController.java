package org.projectcheckins.security.http;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.server.util.HttpHostResolver;
import io.micronaut.http.server.util.locale.HttpLocaleResolver;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.fields.Form;
import io.micronaut.views.fields.FormGenerator;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.annotations.GetHtml;
import org.projectcheckins.annotations.PostForm;
import org.projectcheckins.bootstrap.Breadcrumb;
import org.projectcheckins.security.forms.TeamMemberSave;
import org.projectcheckins.security.forms.TeamMemberUninvite;
import org.projectcheckins.security.services.TeamService;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
class TeamController {

    public static final String SLASH = "/";
    public static final String DOT = ".";
    public static final String DOT_HTML = DOT + "html";
    public static final String ACTION_LIST = "list";
    public static final String ACTION_CREATE = "create";
    public static final String ACTION_SAVE = "save";
    public static final String ACTION_UNINVITE = "uninvite";
    private static final String TEAM = "team";
    public static final String PATH = SLASH + TEAM;

    private static final Message MESSAGE_HOME = Message.of("Home", "home");
    public static final Breadcrumb BREADCRUMB_HOME = new Breadcrumb(MESSAGE_HOME, "/");

    private static final String MODEL_MEMBERS = "members";
    private static final String MODEL_INVITATIONS = "invitations";
    private static final String MODEL_EMAIL = "email";
    private static final String MEMBER_FORM = "memberForm";
    private static final String UNINVITE_FORM = "uninviteForm";
    private static final String MODEL_BREADCRUMBS = "breadcrumbs";

    // LIST
    public static final String PATH_LIST = PATH + SLASH + ACTION_LIST;
    private static final String VIEW_LIST = PATH + SLASH + ACTION_LIST + DOT_HTML;

    private static final Message MESSAGE_LIST = Message.of("Team members", TEAM + DOT + ACTION_LIST);
    public static final Breadcrumb BREADCRUMB_LIST = new Breadcrumb(MESSAGE_LIST, PATH_LIST);
    private static final List<Breadcrumb> BREADCRUMBS_LIST = List.of(BREADCRUMB_HOME, new Breadcrumb(MESSAGE_LIST));

    // CREATE
    private static final String PATH_CREATE = PATH + SLASH + ACTION_CREATE;
    private static final String VIEW_CREATE = PATH + SLASH + ACTION_CREATE + DOT_HTML;
    private static final Breadcrumb BREADCRUMB_CREATE = new Breadcrumb(Message.of("Add team member", TEAM + DOT + ACTION_CREATE));

    // SAVE
    private static final String PATH_SAVE = PATH + SLASH + ACTION_SAVE;

    // UNINVITE
    private static final String PATH_UNINVITE = PATH + SLASH + ACTION_UNINVITE;
    private static final String VIEW_UNINVITE = PATH + SLASH + ACTION_UNINVITE + DOT_HTML;
    private static final Message MESSAGE_UNINVITE = Message.of("Uninvite", TEAM + DOT + ACTION_UNINVITE);
    private static final Breadcrumb BREADCRUMB_UNINVITE = new Breadcrumb(MESSAGE_UNINVITE);

    private final TeamService teamService;
    private final FormGenerator formGenerator;
    private final HttpHostResolver httpHostResolver;
    private final HttpLocaleResolver httpLocaleResolver;

    TeamController(TeamService teamService, FormGenerator formGenerator, HttpHostResolver httpHostResolver, HttpLocaleResolver httpLocaleResolver) {
        this.teamService = teamService;
        this.formGenerator = formGenerator;
        this.httpHostResolver = httpHostResolver;
        this.httpLocaleResolver = httpLocaleResolver;
    }

    @GetHtml(uri = PATH_LIST, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_LIST)
    Map<String, Object> memberList(@Nullable Tenant tenant) {
        return Map.of(
                MODEL_BREADCRUMBS, BREADCRUMBS_LIST,
                MODEL_MEMBERS, teamService.findAll(tenant),
                MODEL_INVITATIONS, teamService.findInvitations(tenant)
        );
    }

    @GetHtml(uri = PATH_CREATE, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_CREATE)
    Map<String, Object> memberCreate() {
        return createModel();
    }

    @PostForm(uri = PATH_SAVE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> memberSave(@NonNull @NotNull @Valid @Body TeamMemberSave form,
                               @NonNull @NotNull HttpRequest<?> request,
                               @Nullable Tenant tenant) {
        teamService.save(form, tenant, getLocale(request), getSignUpUri(request).toString());
        return HttpResponse.seeOther(URI.create(PATH_LIST));
    }

    @GetHtml(uri = PATH_UNINVITE, rolesAllowed = SecurityRule.IS_AUTHENTICATED, view = VIEW_UNINVITE)
    Map<String, Object> memberUninviteForm(@NonNull @NotNull @Email @QueryValue String email) {
        final Form form = formGenerator.generate(PATH_UNINVITE, new TeamMemberUninvite(email), MESSAGE_UNINVITE);
        return Map.of(
                MODEL_BREADCRUMBS, List.of(BREADCRUMB_HOME, BREADCRUMB_LIST, BREADCRUMB_UNINVITE),
                MODEL_EMAIL, email,
                UNINVITE_FORM, form
        );
    }

    @PostForm(uri = PATH_UNINVITE, rolesAllowed = SecurityRule.IS_AUTHENTICATED)
    HttpResponse<?> memberUninvite(@NonNull @NotNull @Valid @Body TeamMemberUninvite form, @Nullable Tenant tenant) {
        teamService.uninvite(form, tenant);
        return HttpResponse.seeOther(URI.create(PATH_LIST));
    }

    @Error(exception = ConstraintViolationException.class)
    public HttpResponse<?> onConstraintViolationException(HttpRequest<?> request, ConstraintViolationException ex) {
        if (PATH_UNINVITE.equals(request.getPath())) {
            return HttpResponse.seeOther(URI.create(PATH_LIST));
        }
        return request.getBody(TeamMemberSave.class)
                .map(form -> HttpResponse.unprocessableEntity().body(new ModelAndView<>(VIEW_CREATE, createModel(form, ex))))
                .orElseGet(HttpResponse::serverError);
    }

    private Map<String, Object> createModel() {
        return createModel(formGenerator.generate(PATH_SAVE, new TeamMemberSave(null)));
    }

    private Map<String, Object> createModel(TeamMemberSave teamMemberSave, ConstraintViolationException ex) {
        return createModel(formGenerator.generate(PATH_SAVE, teamMemberSave, ex));
    }

    private Map<String, Object> createModel(Form form) {
        return Map.of(
                MODEL_BREADCRUMBS, List.of(BREADCRUMB_HOME, BREADCRUMB_LIST, BREADCRUMB_CREATE),
                MEMBER_FORM, form
        );
    }

    private Locale getLocale(HttpRequest<?> request) {
        return httpLocaleResolver.resolveOrDefault(request);
    }

    private URI getSignUpUri(HttpRequest<?> request) {
        return UriBuilder.of(httpHostResolver.resolve(request)).path(SecurityController.PATH_SIGN_UP).build();
    }
}
