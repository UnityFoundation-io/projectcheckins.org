package org.projectcheckins.security;

import io.micronaut.context.event.ApplicationEvent;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.security.forms.TeamMemberSave;

public class InvitationSavedEvent extends ApplicationEvent {

    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String url;

    public InvitationSavedEvent(@NotNull TeamMemberSave form, @NotBlank String url) {
        super(form);
        this.email = form.email();
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public String getUrl() {
        return url;
    }
}
