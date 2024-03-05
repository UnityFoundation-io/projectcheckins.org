package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.eclipsestore.annotations.StoreParams;
import io.micronaut.multitenancy.Tenant;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.function.Predicate;
import org.projectcheckins.core.exceptions.QuestionNotFoundException;
import org.projectcheckins.core.forms.Profile;
import org.projectcheckins.core.forms.ProfileSave;
import org.projectcheckins.core.forms.ProfileUpdate;
import org.projectcheckins.core.idgeneration.IdGenerator;
import org.projectcheckins.core.repositories.ProfileRepository;

@Singleton
class EclipseStoreProfileRepository implements ProfileRepository {
  private final RootProvider<Data> rootProvider;
  private final IdGenerator idGenerator;

  public EclipseStoreProfileRepository(RootProvider<Data> rootProvider, IdGenerator idGenerator) {
    this.rootProvider = rootProvider;
    this.idGenerator = idGenerator;
  }

  @Override
  @NonNull
  public String save(@NotNull @Valid ProfileSave profileSave, @Nullable Tenant tenant) {
    final ProfileEntity entity = toEntity(profileSave);
    rootProvider.root().getProfiles().add(entity);
    save(rootProvider.root().getProfiles());
    return entity.getId();
  }

  @Override
  @NonNull
  public Optional<Profile> findById(@NotBlank String id, @Nullable Tenant tenant) {
    return findFirst(comparing(ProfileEntity::getId, id)).map(this::fromEntity);
  }

  @Override
  @NonNull
  public Optional<Profile> findByEmail(@NotBlank @Email String email, @Nullable Tenant tenant) {
    return findFirst(comparing(ProfileEntity::getEmail, email)).map(this::fromEntity);
  }

  @Override
  public void update(@NotNull @Valid ProfileUpdate profileUpdate, @Nullable Tenant tenant) {
    final ProfileEntity entity = findFirst(comparing(ProfileEntity::getId, profileUpdate.id()))
        .orElseThrow(QuestionNotFoundException::new);
    save(updateEntity(entity, profileUpdate));
  }

  @Override
  public void deleteById(@NotBlank String id, @Nullable Tenant tenant) {
    rootProvider.root().getProfiles().removeIf(p -> p.getId().equals(id));
    save(rootProvider.root().getProfiles());
  }

  @StoreParams("profiles")
  public void save(List<ProfileEntity> profiles) {
  }

  @StoreParams("profile")
  public void save(ProfileEntity profile) {
  }

  private Optional<ProfileEntity> findFirst(Predicate<ProfileEntity> predicate) {
    return rootProvider.root().getProfiles().stream().filter(predicate).findFirst();
  }

  private <T, F> Predicate<T> comparing(Function<T, F> getter, F value) {
    return x -> Objects.equals(getter.apply(x), value);
  }

  private ProfileEntity toEntity(ProfileSave profileSave) {
    ProfileEntity entity = new ProfileEntity();
    String id = idGenerator.generate();
    entity.setId(id);
    entity.setEmail(profileSave.email());
    entity.setAppearance(profileSave.appearance());
    entity.setTimeZone(profileSave.timeZone());
    entity.setFirstDayOfWeek(profileSave.firstDayOfWeek());
    entity.setTimeFormat(profileSave.timeFormat());
    return entity;
  }

  private ProfileEntity updateEntity(ProfileEntity entity, ProfileUpdate profileUpdate) {
    entity.setAppearance(profileUpdate.appearance());
    entity.setTimeZone(TimeZone.getTimeZone(profileUpdate.timeZone()));
    entity.setFirstDayOfWeek(profileUpdate.firstDayOfWeek());
    entity.setTimeFormat(profileUpdate.timeFormat());
    return entity;
  }

  private Profile fromEntity(ProfileEntity entity) {
    return new Profile(
        entity.getId(),
        entity.getEmail(),
        entity.getAppearance(),
        entity.getTimeZone().getID(),
        entity.getFirstDayOfWeek(),
        entity.getTimeFormat()
    );
  }
}
