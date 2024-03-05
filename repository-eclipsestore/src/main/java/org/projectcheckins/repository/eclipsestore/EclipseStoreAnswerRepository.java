package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.eclipsestore.annotations.StoreParams;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.security.authentication.Authentication;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.validation.constraints.PastOrPresent;
import org.projectcheckins.core.exceptions.AnswerNotFoundException;
import org.projectcheckins.core.exceptions.UserNotFoundException;
import org.projectcheckins.core.forms.*;
import org.projectcheckins.core.idgeneration.IdGenerator;
import org.projectcheckins.core.markdown.MarkdownRenderer;
import org.projectcheckins.core.models.Element;
import org.projectcheckins.core.repositories.AnswerRepository;

@Singleton
class EclipseStoreAnswerRepository implements AnswerRepository {
    private final RootProvider<Data> rootProvider;
    private final IdGenerator idGenerator;
    private final MarkdownRenderer markdownRenderer;

    public EclipseStoreAnswerRepository(RootProvider<Data> rootProvider,
                                        IdGenerator idGenerator,
                                        MarkdownRenderer markdownRenderer) {
        this.rootProvider = rootProvider;
        this.idGenerator = idGenerator;
        this.markdownRenderer = markdownRenderer;
    }

    @Override
    @NonNull
    public Optional<Answer> findById(@NotBlank String answerId, @Nullable Tenant tenant) {
        return findEntityById(answerId)
                .map(this::toAnswer);
    }

    @Override
    @NonNull
    public List<Answer> findByQuestionId(@NotBlank String questionId, @Nullable Tenant tenant) {
        return EclipseStoreQuestionRepository.findEntityById(rootProvider, questionId)
                .map(QuestionEntity::getAnswers)
                .orElse(List.of())
                .stream()
                .map(this::toAnswer)
                .toList();
    }

    private Answer toAnswer(AnswerEntity answerEntity) {
        String html = answerEntity.getText();
        if (answerEntity.getFormat() == Format.MARKDOWN) {
            html = markdownRenderer.render(html);
        }
        return new Answer(answerEntity.getId(),
                answerEntity.getQuestionId(),
                findUserEntityById(rootProvider, answerEntity.getUserId())
                        .map(userEntity -> new Element(userEntity.getId(), userEntity.getEmail()))
                        .orElseThrow(UserNotFoundException::new),
                answerEntity.getAnswerDate(),
                html);
    }

    @Override
    @NonNull
    public String save(@NonNull @NotNull @Valid AnswerSave answerSave,
                @NonNull Authentication authentication,
                @Nullable Tenant tenant) {
        final String id = idGenerator.generate();
        AnswerEntity entity = fromAnswer(id, answerSave, authentication);
        EclipseStoreQuestionRepository.findEntityById(rootProvider, answerSave.questionId()).ifPresent(questionEntity -> {
            questionEntity.getAnswers().add(entity);
            save(questionEntity.getAnswers());
        });
        return id;
    }

    @Override
    public void update(@NotNull @Valid AnswerUpdate answerUpdate, @Nullable Tenant tenant) {
        AnswerEntity answer = findEntityById(answerUpdate.id())
                .orElseThrow(AnswerNotFoundException::new);
        answer.setAnswerDate(answerUpdate.answerDate());
        answer.setText(answerUpdate.text());
        save(answer);
    }

    @Override
    public void deleteById(@NotBlank String id, @Nullable Tenant tenant) {
        for (QuestionEntity questionEntity : rootProvider.root().getQuestions()) {
            questionEntity.getAnswers().removeIf(a -> a.getId().equals(id));
            save(questionEntity.getAnswers());
        }
    }

    @Override
    public boolean hasAnswered(@NotBlank String questionId,
                        @NotNull@PastOrPresent LocalDate answerDate,
                        @NonNull @NotNull Authentication authentication,
                        @Nullable Tenant tenant) {
        return EclipseStoreQuestionRepository.findEntityById(rootProvider, questionId)
                .map(QuestionEntity::getAnswers)
                .orElse(List.of())
                .stream()
                .anyMatch(answerEntity -> answerEntity.getUserId().equals(authentication.getName()) && answerEntity.getAnswerDate().equals(answerDate));
    }

    private Optional<AnswerEntity> findEntityById(@NotBlank String id) {
        for (QuestionEntity questionEntity : rootProvider.root().getQuestions()) {
            for (AnswerEntity answerEntity : questionEntity.getAnswers()) {
                if (answerEntity.getId().equals(id)) {
                    return Optional.of(answerEntity);
                }
            }
        }
        return Optional.empty();
    }

    @StoreParams("answers")
    public void save(List<AnswerEntity> answers) {
    }

    @StoreParams("answer")
    public void save(AnswerEntity answer) {
    }

    @NonNull
    private static Optional<UserEntity> findUserEntityById(RootProvider<Data> rootProvider, @NotBlank @NonNull String id) {
        return rootProvider.root().getUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    @NonNull
    public Optional<AnswerUpdate> findAnswerUpdate(@NotBlank String questionId,
                                                   @NotBlank String id,
                                                   @Nullable Tenant tenant) {
        return EclipseStoreQuestionRepository.findEntityById(rootProvider, questionId)
                .map(QuestionEntity::getAnswers)
                .orElse(List.of())
                .stream()
                .filter(answerEntity -> answerEntity.getId().equals(id))
                .map(EclipseStoreAnswerRepository::answerUpdateOf)
                .findFirst();
    }

    private static AnswerUpdate answerUpdateOf(AnswerEntity answerEntity) {
        return new AnswerUpdate(answerEntity.getQuestionId(),
                answerEntity.getId(),
                answerEntity.getFormat(),
                answerEntity.getAnswerDate(),
                answerEntity.getText());
    }

    public static AnswerEntity fromAnswer(@NotBlank String id,
                                          @NonNull AnswerSave answerSave,
                                          @NonNull Authentication authentication) {
        AnswerEntity entity = new AnswerEntity();
        entity.setId(id);
        entity.setFormat(answerSave.format());
        entity.setQuestionId(answerSave.questionId());
        entity.setUserId(authentication.getName());
        entity.setAnswerDate(answerSave.answerDate());
        entity.setText(answerSave.text());
        return entity;
    }

}
