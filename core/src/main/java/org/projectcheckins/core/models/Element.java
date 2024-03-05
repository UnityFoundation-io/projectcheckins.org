package org.projectcheckins.core.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record Element(@NotBlank String id, @NotBlank String name) {
}
