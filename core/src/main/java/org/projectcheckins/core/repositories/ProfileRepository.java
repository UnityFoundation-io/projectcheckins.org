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
import org.projectcheckins.core.forms.ProfileSave;
import org.projectcheckins.core.forms.ProfileUpdate;

public interface ProfileRepository {

  @NonNull
  String save(@NotNull @Valid ProfileSave profileSave, @Nullable Tenant tenant);

  @NonNull
  Optional<Profile> findById(@NotBlank String id, @Nullable Tenant tenant);

  @NonNull
  Optional<Profile> findByEmail(@NotBlank @Email String email, @Nullable Tenant tenant);

  void update(@NotNull @Valid ProfileUpdate profileUpdate, @Nullable Tenant tenant);

  void deleteById(@NotBlank String id, @Nullable Tenant tenant);

  @NonNull
  default String save(@NotNull @Valid ProfileSave profileSave) {
    return save(profileSave, null);
  }

  @NonNull
  default Optional<Profile> findById(@NotBlank String id) {
    return findById(id, null);
  }

  @NonNull
  default Optional<Profile> findByEmail(@NotBlank @Email String email) {
    return findByEmail(email, null);
  }

  default void update(@NotNull @Valid ProfileUpdate profileUpdate) {
    update(profileUpdate, null);
  }

  default void deleteById(@NotBlank String id) {
    deleteById(id, null);
  }
}
