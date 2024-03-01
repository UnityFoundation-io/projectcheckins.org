package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.eclipsestore.annotations.StoreParams;
import jakarta.inject.Singleton;
import org.projectcheckins.core.idgeneration.IdGenerator;
import org.projectcheckins.security.*;

import java.util.Collections;
import java.util.List;

@Singleton
class EclipseStoreRegisterService extends AbstractRegisterService implements UserRepository {

    private final RootProvider<Data> rootProvider;
    private final IdGenerator idGenerator;
    protected EclipseStoreRegisterService(PasswordEncoder passwordEncoder,
                                          RootProvider<Data> rootProvider,
                                          IdGenerator idGenerator) {
        super(passwordEncoder);
        this.rootProvider = rootProvider;
        this.idGenerator = idGenerator;
    }

    @Override
    public String register(@NonNull UserSave userSave) throws UserAlreadyExistsException {
        if (rootProvider.root().getUsers().stream().anyMatch(user -> user.getEmail().equals(userSave.email()))) {
            throw new UserAlreadyExistsException();
        }
        String id = idGenerator.generate();
        UserEntity userEntity = entityOf(userSave);
        userEntity.setId(id);
        saveUser(rootProvider.root().getUsers(), userEntity);
        return id;
    }

    @Override
    public void enable(String userId) {
        rootProvider.root().getUsers().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .ifPresent(this::enableUser);
    }

    @StoreParams("users")
    public void saveUser(List<UserEntity> users, UserEntity userEntity) {
        users.add(userEntity);
    }

    @StoreParams("user")
    public void enableUser(UserEntity user) {
        user.setEnabled(true);
    }

    @NonNull
    private UserEntity entityOf(@NonNull UserSave userSave) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userSave.email());
        userEntity.setEncodedPassword(userSave.encodedPassword());
        userEntity.setAuthorities(userSave.authorities());
        return userEntity;
    }
}
