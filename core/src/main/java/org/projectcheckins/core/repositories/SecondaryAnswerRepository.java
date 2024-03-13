package org.projectcheckins.core.repositories;

import io.micronaut.context.annotation.Secondary;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import org.projectcheckins.core.forms.AnswerSave;

@Secondary
@Singleton
public class SecondaryAnswerRepository implements AnswerRepository {
    @Override
    public void save(Authentication authentication, AnswerSave answerSave) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
