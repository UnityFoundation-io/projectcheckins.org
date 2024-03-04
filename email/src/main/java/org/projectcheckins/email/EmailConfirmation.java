package org.projectcheckins.email;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;

@Introspected
public record EmailConfirmation(@NonNull @NotBlank String url,
                                @NonNull @NotBlank String email) {
}
