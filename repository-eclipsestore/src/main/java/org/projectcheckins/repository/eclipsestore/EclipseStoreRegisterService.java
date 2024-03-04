package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.eclipsestore.RootProvider;
import jakarta.inject.Singleton;
import org.projectcheckins.core.idgeneration.IdGenerator;
import org.projectcheckins.security.AbstractRegisterService;
import org.projectcheckins.security.PasswordEncoder;
import org.projectcheckins.security.UserAlreadyExistsException;
import org.projectcheckins.security.UserSave;

@Singleton
class EclipseStoreRegisterService extends AbstractRegisterService {

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
    protected String register(@NonNull UserSave userSave) throws UserAlreadyExistsException {
        if (rootProvider.root().getUsers().stream().anyMatch(user -> user.email().equals(userSave.email()))) {
            throw new UserAlreadyExistsException();
        }
        String id = idGenerator.generate();
        rootProvider.root().getUsers().add(new UserEntity(id, userSave.email(), userSave.encodedPassword(), userSave.authorities()));
        return id;
    }
}
