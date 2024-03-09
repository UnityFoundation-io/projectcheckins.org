package org.projectcheckins.security;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

public abstract class AbstractRegisterService implements RegisterService {

    private final PasswordEncoder passwordEncoder;

    protected AbstractRegisterService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String register(@NonNull @NotBlank String username,
                           @NonNull @NotBlank String rawPassword) throws UserAlreadyExistsException {
        return register(username, rawPassword, Collections.emptyList());
    }

    @NonNull
    protected abstract String register(@NonNull @NotNull @Valid UserSave userSave) throws UserAlreadyExistsException;

    @Override
    public String register(@NonNull @NotBlank String username,
                           @NonNull @NotBlank String rawPassword,
                           @NonNull List<String> authorities) throws UserAlreadyExistsException {
        final String encodedPassword = passwordEncoder.encode(rawPassword);
        return register(new UserSave(username, encodedPassword, authorities, TimeZone.getDefault(), DayOfWeek.SUNDAY, false));
    }
}
