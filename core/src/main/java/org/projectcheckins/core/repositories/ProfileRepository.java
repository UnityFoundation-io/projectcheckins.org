package org.projectcheckins.core.repositories;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import org.projectcheckins.core.forms.Profile;
import org.projectcheckins.core.forms.ProfileUpdate;

public interface ProfileRepository {

  @NonNull
  Optional<Profile> findByEmail(@NotBlank @Email String email, @Nullable Tenant tenant);

  void update(@NotBlank @Email String email, @NotNull @Valid ProfileUpdate profileUpdate, @Nullable Tenant tenant);

  @NonNull
  default Optional<Profile> findByEmail(@NotBlank @Email String email) {
    return findByEmail(email, null);
  }

  default void update(@NotBlank @Email String email, @NotNull @Valid ProfileUpdate profileUpdate) {
    update(email, profileUpdate, null);
  }
}
