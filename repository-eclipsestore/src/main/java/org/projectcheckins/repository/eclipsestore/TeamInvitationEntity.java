package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.security.TeamInvitation;

@Introspected
public class TeamInvitationEntity implements TeamInvitation {

    @NotBlank
    private String email;
    private boolean accepted;

    public TeamInvitationEntity(String email) {
        this.email = email;
    }

    public String email() {
        return email;
    }

    public void email(String email) {
        this.email = email;
    }

    public boolean accepted() {
        return accepted;
    }

    public void accepted(boolean accepted) {
        this.accepted = accepted;
    }
}
