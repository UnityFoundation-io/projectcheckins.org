package org.projectcheckins.core.repositories;

import io.micronaut.context.annotation.Secondary;
import io.micronaut.multitenancy.Tenant;
import jakarta.inject.Singleton;
import org.projectcheckins.core.forms.Question;
import org.projectcheckins.core.forms.QuestionSave;
import org.projectcheckins.core.forms.QuestionUpdate;
import org.projectcheckins.core.models.Element;

import java.util.List;
import java.util.Optional;

@Singleton
@Secondary
public class SecondaryQuestionRepository implements QuestionRepository {
    @Override
    public String save(QuestionSave questionSave, Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Optional<Question> findById(String id, Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void update(QuestionUpdate questionUpdate, Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<Question> findAll(Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteById(String id, Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Optional<Element> findElementById(String questionId, Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
