package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class AbstractRegisterService implements RegisterService {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRegisterService.class);

    private final PasswordEncoder passwordEncoder;
    private final List<RegistrationCheck> registrationChecks;
    private final TeamInvitationRepository teamInvitationRepository;

    protected AbstractRegisterService(
            PasswordEncoder passwordEncoder,
            List<RegistrationCheck> registrationChecks,
            TeamInvitationRepository teamInvitationRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.registrationChecks = registrationChecks;
        this.teamInvitationRepository = teamInvitationRepository;
    }

    public String register(@NonNull @NotBlank String username,
                           @NonNull @NotBlank String rawPassword) throws RegistrationCheckViolationException {
        return register(username, rawPassword, Collections.emptyList());
    }

    @NonNull
    protected abstract String register(@NonNull @NotNull @Valid UserSave userSave) throws RegistrationCheckViolationException;

    @Override
    public String register(@NonNull @NotBlank String username,
                           @NonNull @NotBlank String rawPassword,
                           @NonNull List<String> authorities) throws RegistrationCheckViolationException {
        Optional<RegistrationCheckViolation> violationOptional = registrationChecks.stream()
                .map(check -> check.validate(username))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
        if (violationOptional.isPresent()) {
            LOG.warn("Could not register {}", violationOptional.get().message().defaultMessage());
            throw new RegistrationCheckViolationException(violationOptional.get());
        }
        final String encodedPassword = passwordEncoder.encode(rawPassword);
        final String id = register(new UserSave(username, encodedPassword, authorities));
        teamInvitationRepository.deleteByEmail(username);
        return id;
    }
}
