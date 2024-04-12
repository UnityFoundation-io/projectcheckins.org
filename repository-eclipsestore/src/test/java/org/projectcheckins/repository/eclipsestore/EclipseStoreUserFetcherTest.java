package org.projectcheckins.repository.eclipsestore;

import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.idgeneration.IdGenerator;
import org.projectcheckins.email.EmailConfirmationRepository;
import org.projectcheckins.security.RegisterService;
import org.projectcheckins.security.TeamInvitationRepository;
import org.projectcheckins.security.UserAlreadyExistsException;
import org.projectcheckins.security.UserFetcher;
import org.projectcheckins.security.UserNotInvitedException;

import static org.assertj.core.api.Assertions.*;

@MicronautTest(startApplication = false)
class EclipseStoreUserFetcherTest {
    private final static String NOT_FOUND_EMAIL = "delamos@unityfoundation.io";
    private final static String FOUND_EMAIL = "calvog@unityfoundation.io";

    @Inject
    TeamInvitationRepository teamInvitationRepository;

    @Test
    void authoritiesFetcher(UserFetcher userFetcher, RegisterService registerService) throws UserAlreadyExistsException, UserNotInvitedException {
        assertThat(userFetcher.findByEmail(NOT_FOUND_EMAIL))
            .isEmpty();
        teamInvitationRepository.save(FOUND_EMAIL);
        registerService.register(FOUND_EMAIL, "password");
        assertThat(userFetcher.findByEmail(FOUND_EMAIL)).hasValueSatisfying(userState -> assertThat(userState)
                .matches(u -> !u.isEnabled())
                .matches(u -> u.getEmail().equals(FOUND_EMAIL))
                .matches(u -> u.getId() != null)
                .matches(u -> u.getPassword() != null && !u.getPassword().equals("password")));
    }

    @Test
    void testRegister(RegisterService registerService) {
        String email = "sergio@projectcheckins.org";
        String notInvited = "not-invited@projectcheckins.org";
        teamInvitationRepository.save(email);
        assertThatCode(() -> registerService.register(email, "foo"))
                .doesNotThrowAnyException();
        assertThatThrownBy(() -> registerService.register(email, "foo"))
                .isInstanceOf(UserAlreadyExistsException.class);
        assertThatThrownBy(() -> registerService.register(notInvited, "foo"))
                .isInstanceOf(UserNotInvitedException.class);
    }

    @Test
    void testUserEnable(RootProvider<Data> rootProvider,
                        RegisterService registerService,
                        IdGenerator idGenerator,
                        EmailConfirmationRepository emailConfirmationRepository) throws UserAlreadyExistsException, UserNotInvitedException {
        String email = idGenerator.generate() + "@projectcheckins.org";
        teamInvitationRepository.save(email);
        String id = registerService.register(email, "password");
        assertThat(rootProvider.root().getUsers()).noneMatch(UserEntity::enabled);
        emailConfirmationRepository.enableByEmail(email);
        assertThat(rootProvider.root().getUsers()).anyMatch(UserEntity::enabled);

    }
}
