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
import java.util.List;
import java.util.Optional;
import org.projectcheckins.core.exceptions.AnswerNotFoundException;
import org.projectcheckins.core.forms.Answer;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.forms.AnswerUpdate;
import org.projectcheckins.core.idgeneration.IdGenerator;
import org.projectcheckins.core.repositories.AnswerRepository;

@Singleton
class EclipseStoreAnswerRepository implements AnswerRepository {
  private final RootProvider<Data> rootProvider;
  private final IdGenerator idGenerator;

  public EclipseStoreAnswerRepository(RootProvider<Data> rootProvider, IdGenerator idGenerator) {
    this.rootProvider = rootProvider;
    this.idGenerator = idGenerator;
  }

  @Override
  @NonNull
  public Optional<Answer> findById(@NotBlank String answerId, @Nullable Tenant tenant) {
    return findEntityById(answerId)
        .map(AnswerEntity::toAnswer);
  }

  @Override
  @NonNull
  public List<Answer> findByQuestionId(@NotBlank String questionId, @Nullable Tenant tenant) {
    return rootProvider.root().getAnswers()
        .stream()
        .filter(a -> a.getQuestionId().equals(questionId))
        .map(AnswerEntity::toAnswer)
        .toList();
  }

  @Override
  public String save(AnswerSave answerSave, Authentication authentication, @Nullable Tenant tenant) {
    final String id = idGenerator.generate();
    AnswerEntity entity = AnswerEntity.fromAnswer(id, answerSave, authentication);
    rootProvider.root().getAnswers().add(entity);
    save(rootProvider.root().getAnswers());
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
    rootProvider.root().getAnswers().removeIf(a -> a.getId().equals(id));
    save(rootProvider.root().getAnswers());
  }

  private Optional<AnswerEntity> findEntityById(@NotBlank String id) {
    return rootProvider.root().getAnswers()
        .stream()
        .filter(a -> a.getId().equals(id))
        .findFirst();
  }

  @StoreParams("answers")
  public void save(List<AnswerEntity> answers) {
  }

  @StoreParams("answer")
  public void save(AnswerEntity answer) {
  }
}
