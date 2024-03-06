package org.projectcheckins.core.repositories;

import io.micronaut.context.annotation.Secondary;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.Nullable;
import org.projectcheckins.core.forms.Answer;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.forms.AnswerUpdate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Secondary
@Singleton
public class SecondaryAnswerRepository implements AnswerRepository {
    @Override
    public Optional<Answer> findById(String answerId, @Nullable Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<Answer> findByQuestionId(String questionId, @Nullable Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String save(AnswerSave answerSave, Authentication authentication, @Nullable Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void update(AnswerUpdate answerUpdate, @Nullable Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteById(String id, @Nullable Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Optional<AnswerUpdate> findAnswerUpdate(String questionId, String id, @Nullable Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean hasAnswered(String questionId, LocalDate answerDate, Authentication authentication, @Nullable Tenant tenant) {
        return false;
    }
}
