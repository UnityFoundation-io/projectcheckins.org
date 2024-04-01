package org.projectcheckins.core.repositories;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Secondary;
import io.micronaut.context.env.Environment;
import io.micronaut.multitenancy.Tenant;
import jakarta.inject.Singleton;
import org.projectcheckins.annotations.Generated;
import org.projectcheckins.core.api.Answer;

import java.util.List;


@Generated // "ignore for jacoco"
@Requires(env = Environment.TEST)
@Secondary
@Singleton
public class SecondaryAnswerRepository implements AnswerRepository {
    @Override
    public String save(Answer answer, Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<? extends Answer> findByQuestionId(String questionId, Tenant tenant) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
