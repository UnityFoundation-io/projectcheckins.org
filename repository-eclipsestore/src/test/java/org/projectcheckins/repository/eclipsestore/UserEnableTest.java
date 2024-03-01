package org.projectcheckins.repository.eclipsestore;

import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.projectcheckins.security.RegisterService;
import org.projectcheckins.security.UserAlreadyExistsException;
import org.projectcheckins.security.UserRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class UserEnableTest {

    @Test
    void testUserEnable(RootProvider<Data> rootProvider,
                        RegisterService registerService,
                        UserRepository userRepository) throws UserAlreadyExistsException {
        String id = registerService.register("user", "password");
        assertTrue(rootProvider.root().getUsers().stream().noneMatch(UserEntity::isEnabled));
        userRepository.enable(id);
        assertTrue(rootProvider.root().getUsers().stream().anyMatch(UserEntity::isEnabled));

    }
}
