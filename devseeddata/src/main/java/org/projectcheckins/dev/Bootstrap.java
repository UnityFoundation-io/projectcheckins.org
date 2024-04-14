package org.projectcheckins.dev;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.ApplicationEventListener;

import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Singleton;
import org.projectcheckins.email.EmailConfirmationRepository;
import org.projectcheckins.security.RegisterService;
import org.projectcheckins.security.RegistrationCheckViolationException;
import org.projectcheckins.security.TeamInvitationRepository;
import org.projectcheckins.annotations.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Generated// "ignore for jacoco"
@Requires(env = Environment.DEVELOPMENT)
@Singleton
public class Bootstrap implements ApplicationEventListener<ServerStartupEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);

    private final RegisterService registerService;
    private final TeamInvitationRepository teamInvitationRepository;
    private final EmailConfirmationRepository emailConfirmationRepository;

    public Bootstrap(RegisterService registerService,
                     TeamInvitationRepository teamInvitationRepository,
                     EmailConfirmationRepository emailConfirmationRepository) {
        this.registerService = registerService;
        this.teamInvitationRepository = teamInvitationRepository;
        this.emailConfirmationRepository = emailConfirmationRepository;
    }

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        teamInvitationRepository.save("pending@example.com");
        addUser("delamos@unityfoundation.io");
        addUser("calvog@unityfoundation.io");
        addUser("grellej@unityfoundation.io");
        addUser("yatest@unityfoundation.io");
        addUser("wetted@objectcomputing.com");
    }

    private void addUser(String email) {
        try {
            teamInvitationRepository.save(email);
            registerService.register(email, "secret");
        } catch (RegistrationCheckViolationException e) {
            LOG.warn("{}", e.getViolation().message().defaultMessage());
        }
        emailConfirmationRepository.enableByEmail(email);
    }
}