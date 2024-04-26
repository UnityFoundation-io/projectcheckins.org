package org.projectcheckins.security.http;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.Form;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.security.TeamInvitation;

@Serdeable
public record InvitationRow(@NonNull @NotNull @Valid TeamInvitation invite,
                            @Nullable Form deleteForm) {
}
