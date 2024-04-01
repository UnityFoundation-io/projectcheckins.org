package org.projectcheckins.core.services;

import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import org.projectcheckins.core.api.Answer;
import org.projectcheckins.core.forms.*;
import org.projectcheckins.core.repositories.AnswerRepository;

import java.util.List;

@Singleton
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public String save(Authentication authentication, AnswerSave answerSave, Tenant tenant) {
        final AnswerRecord answer = new AnswerRecord(
                null,
                answerSave.questionId(),
                authentication.getName(),
                answerSave.answerDate(),
                answerSave.format(),
                answerSave.text()
        );
        return answerRepository.save(answer, tenant);
    }

    @Override
    public List<? extends Answer> findByQuestionId(String questionId, Tenant tenant) {
        return answerRepository.findByQuestionId(questionId, tenant);
    }
}
