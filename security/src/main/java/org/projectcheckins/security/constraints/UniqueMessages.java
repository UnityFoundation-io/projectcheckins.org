package org.projectcheckins.security.constraints;

import io.micronaut.context.StaticMessageSource;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

@Singleton
public class UniqueMessages extends StaticMessageSource {

    public static final String UNIQUE_MESSAGES = "Invitation already exists";

    private static final String MESSAGE_SUFFIX = ".message";

    public UniqueMessages() {
    }

    @PostConstruct
    void init() {
        addMessage(Unique.class.getName() + MESSAGE_SUFFIX, UNIQUE_MESSAGES);
    }
}
