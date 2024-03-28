package org.projectcheckins.core.services;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.multitenancy.Tenant;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.api.PublicProfile;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.exceptions.UserNotFoundException;
import org.projectcheckins.core.forms.QuestionForm;
import org.projectcheckins.core.forms.QuestionRecord;
import org.projectcheckins.core.forms.RespondentRecord;
import org.projectcheckins.core.repositories.ProfileRepository;
import org.projectcheckins.core.repositories.QuestionRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toSet;

@Singleton
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final ProfileRepository profileRepository;
    private final SchedulingService schedulingService;

    public QuestionServiceImpl(QuestionRepository questionRepository, ProfileRepository profileRepository, SchedulingService schedulingService) {
        this.questionRepository = questionRepository;
        this.profileRepository = profileRepository;
        this.schedulingService = schedulingService;
    }

    @Override
    @NonNull
    public String createQuestion(@NotNull @Valid QuestionForm form, @Nullable Tenant tenant) {
        return questionRepository.save(question(null, form, tenant), tenant);
    }

    @Override
    @NonNull
    public Optional<? extends Question> getQuestion(@NotBlank String id, @Nullable Tenant tenant) {
        return questionRepository.findById(id, tenant);
    }

    @Override
    public void updateQuestion(@NotBlank String id, @NotNull @Valid QuestionForm form, @Nullable Tenant tenant) {
        questionRepository.update(question(id, form, tenant), tenant);
    }

    @Override
    @NonNull
    public List<? extends Question> listQuestions(@Nullable Tenant tenant) {
        return questionRepository.findAll(tenant);
    }

    @Override
    public void deleteQuestion(@NotBlank String id, @Nullable Tenant tenant) {
        questionRepository.deleteById(id, tenant);
    }

    @Override
    @NonNull
    public List<? extends PublicProfile> listAvailableRespondents(@Nullable Tenant tenant) {
        return profileRepository.list(tenant);
    }

    private QuestionRecord question(@Nullable String questionId, @NotNull QuestionForm form, @Nullable Tenant tenant) {
        return new QuestionRecord(
                questionId,
                form.title(),
                form.howOften(),
                form.days(),
                form.timeOfDay(),
                form.fixedTime(),
                Optional.ofNullable(form.respondentIds()).orElseGet(Collections::emptySet).stream()
                        .map(id -> createRespondent(id, form, tenant)).collect(toSet()));
    }

    private RespondentRecord createRespondent(@NotBlank String profileId, @NonNull QuestionForm form, @Nullable Tenant tenant) {
        final Profile profile = profileRepository.findById(profileId, tenant).orElseThrow(UserNotFoundException::new);
        return new RespondentRecord(
                profileId,
                schedulingService.nextExecution(
                        null,
                        form.howOften(),
                        form.days(),
                        profile.timeZone().toZoneId(),
                        form.timeOfDay().getTime(form.fixedTime(), profile)
                ));
    }
}
