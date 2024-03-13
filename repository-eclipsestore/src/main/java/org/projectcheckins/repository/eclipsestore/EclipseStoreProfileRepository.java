package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.eclipsestore.annotations.StoreParams;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.exceptions.UserNotFoundException;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.Profile;
import org.projectcheckins.core.forms.ProfileUpdate;
import org.projectcheckins.core.repositories.ProfileRepository;

import java.util.Optional;

@Singleton
class EclipseStoreProfileRepository implements ProfileRepository {
    private final RootProvider<Data> rootProvider;

    public EclipseStoreProfileRepository(RootProvider<Data> rootProvider) {
        this.rootProvider = rootProvider;
    }

    @Override
    @NonNull
    public Format findPreferedFormat(@NonNull @NotNull Authentication authentication) {
        return findById(authentication.getName()).map(UserEntity::getFormat).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Optional<Profile> findByEmail(String email) {
        return rootProvider.root().getUsers().stream().filter(u -> u.getEmail().equals(email)).map(this::fromEntity).findFirst();
    }

    @Override
    public void update(@NotNull Authentication authentication, @NotNull @Valid ProfileUpdate profileUpdate, @Nullable Tenant tenant) {
        final UserEntity entity = findById(authentication.getName()).orElseThrow(UserNotFoundException::new);
        save(updateEntity(entity, profileUpdate));
    }

    @Override
    @NonNull
    public Optional<Profile> findByAuthentication(@NotNull Authentication authentication, @Nullable Tenant tenant) {
        return findById(authentication.getName()).map(this::fromEntity);
    }

    @StoreParams("user")
    public void save(UserEntity user) {
    }

    @NonNull
    private Optional<UserEntity> findById(@NonNull String id) {
        return rootProvider.root().getUsers().stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    private UserEntity updateEntity(UserEntity entity, ProfileUpdate profileUpdate) {
        entity.setTimeZone(profileUpdate.timeZone());
        entity.setFirstDayOfWeek(profileUpdate.firstDayOfWeek());
        entity.setBeginningOfDay(profileUpdate.beginningOfDay());
        entity.setEndOfDay(profileUpdate.endOfDay());
        entity.setTimeFormat(profileUpdate.timeFormat());
        entity.setFirstName(profileUpdate.firstName());
        entity.setLastName(profileUpdate.lastName());
        return entity;
    }

    private Profile fromEntity(UserEntity entity) {
        return new Profile(
                entity.getId(),
                entity.getEmail(),
                entity.getTimeZone(),
                entity.getFirstDayOfWeek(),
                entity.getBeginningOfDay(),
                entity.getEndOfDay(),
                entity.getTimeFormat(),
                entity.getFormat(),
                entity.getFirstName(),
                entity.getLastName()
        );
    }
}
