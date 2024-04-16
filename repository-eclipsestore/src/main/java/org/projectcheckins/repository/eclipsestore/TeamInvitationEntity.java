package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.NotBlank;
import org.projectcheckins.security.TeamInvitation;
import org.projectcheckins.security.constraints.Unique;

@Introspected
public class TeamInvitationEntity implements TeamInvitation {

    @NotBlank
    private String email;

    public TeamInvitationEntity(String email) {
        this.email = email;
    }

    public String email() {
        return email;
    }

    public void email(String email) {
        this.email = email;
    }
}
