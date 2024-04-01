package org.projectcheckins.repository.eclipsestore;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.eclipsestore.annotations.StoreParams;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.forms.AnswerMarkdownSave;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.idgeneration.IdGenerator;
import org.projectcheckins.core.repositories.AnswerRepository;

import java.util.List;

@Singleton
class EclipseStoreAnswerRepository implements AnswerRepository {
    private final RootProvider<Data> rootProvider;
    private final IdGenerator idGenerator;

    public EclipseStoreAnswerRepository(RootProvider<Data> rootProvider,
                                        IdGenerator idGenerator) {
        this.rootProvider = rootProvider;
        this.idGenerator = idGenerator;
    }

    @Override
    public void save(@NonNull @NotNull Authentication authentication,
              @NonNull @NotNull @Valid AnswerSave answerSave) {
        AnswerEntity entity = new AnswerEntity();
        String id = idGenerator.generate();
        entity.id(id);
        entity.questionId(answerSave.questionId());
        entity.respondentId(authentication.getName());
        entity.answerDate(answerSave.answerDate());
        entity.format(answerSave.format());
        entity.text(answerSave.text());
        rootProvider.root().getAnswers().add(entity);
        save(rootProvider.root().getAnswers());
    }

    @StoreParams("answers")
    public void save(List<AnswerEntity> answers) {
    }

    @StoreParams("answer")
    public void save(AnswerMarkdownSave answer) {
    }
}
