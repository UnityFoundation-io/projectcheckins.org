package org.projectcheckins.core.services;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.multitenancy.Tenant;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.api.Profile;
import org.projectcheckins.core.api.Question;
import org.projectcheckins.core.api.Respondent;
import org.projectcheckins.core.forms.QuestionForm;
import org.projectcheckins.core.forms.QuestionFormRecord;
import org.projectcheckins.core.forms.QuestionRecord;
import org.projectcheckins.core.forms.RespondentRecord;
import org.projectcheckins.core.repositories.ProfileRepository;
import org.projectcheckins.core.repositories.QuestionRepository;
import org.projectcheckins.core.repositories.SecondaryProfileRepository;
import org.projectcheckins.core.repositories.SecondaryQuestionRepository;

import java.time.LocalTime;
import java.util.*;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.ZonedDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.projectcheckins.core.forms.HowOften.DAILY_ON;
import static org.projectcheckins.core.forms.HowOften.EVERY_OTHER_WEEK;
import static org.projectcheckins.core.forms.TimeOfDay.BEGINNING;
import static org.projectcheckins.core.forms.TimeOfDay.FIXED;

@Property(name = "spec.name", value = "QuestionServiceImplTest")
@MicronautTest(startApplication = false)
class QuestionServiceImplTest {

    static final Set<Respondent> RESPONDENTS = Set.of(
            new RespondentRecord("user1", now()),
            new RespondentRecord("user2", now())
    );

    final Question QUESTION = new QuestionRecord(null, "existing title", DAILY_ON, Set.of(FRIDAY), FIXED, LocalTime.of(12, 30), RESPONDENTS);

    @Inject
    QuestionServiceImpl questionService;

    @Inject
    QuestionRepository questionRepository;

    @Test
    void testUpdate() {
        final String id = questionRepository.save(QUESTION);
        final String updatedTitle = "UPDATED TITLE";
        final QuestionForm updateTitleOnlyForm = QuestionFormRecord.of(new QuestionRecord(id, updatedTitle, QUESTION.howOften(), QUESTION.days(), QUESTION.timeOfDay(), QUESTION.fixedTime(), QUESTION.respondents()));
        final QuestionForm updateHowOftenOnly = QuestionFormRecord.of(new QuestionRecord(id, updatedTitle, EVERY_OTHER_WEEK, QUESTION.days(), QUESTION.timeOfDay(), QUESTION.fixedTime(), QUESTION.respondents()));
        final QuestionForm updateDaysOnly = QuestionFormRecord.of(new QuestionRecord(id, updatedTitle, QUESTION.howOften(), Set.of(MONDAY), QUESTION.timeOfDay(), QUESTION.fixedTime(), QUESTION.respondents()));
        final QuestionForm updateTimeOfDayOnly = QuestionFormRecord.of(new QuestionRecord(id, updatedTitle, QUESTION.howOften(), QUESTION.days(), BEGINNING, QUESTION.fixedTime(), QUESTION.respondents()));
        final QuestionForm updateFixedTimeOnly = QuestionFormRecord.of(new QuestionRecord(id, updatedTitle, QUESTION.howOften(), QUESTION.days(), QUESTION.timeOfDay(), LocalTime.of(21, 45), QUESTION.respondents()));
        final QuestionForm updateRespondentsOnly = QuestionFormRecord.of(new QuestionRecord(id, updatedTitle, QUESTION.howOften(), QUESTION.days(), QUESTION.timeOfDay(), LocalTime.of(21, 45), Set.of(new RespondentRecord("user3", now()))));

        questionService.updateQuestion(id, updateTitleOnlyForm, null);
        assertThat(questionRepository.findById(id))
                .hasValueSatisfying(q -> assertThat(q).hasFieldOrPropertyWithValue("title", updatedTitle));

        assertThatThrownBy(() -> questionService.updateQuestion(id, updateHowOftenOnly, null))
                .hasMessage("Next execution time calculated");

        assertThatThrownBy(() -> questionService.updateQuestion(id, updateDaysOnly, null))
                .hasMessage("Next execution time calculated");

        assertThatThrownBy(() -> questionService.updateQuestion(id, updateTimeOfDayOnly, null))
                .hasMessage("Next execution time calculated");

        assertThatThrownBy(() -> questionService.updateQuestion(id, updateFixedTimeOnly, null))
                .hasMessage("Next execution time calculated");

        assertThatThrownBy(() -> questionService.updateQuestion(id, updateRespondentsOnly, null))
                .hasMessage("Next execution time calculated");
    }

    @Requires(property = "spec.name", value = "QuestionServiceImplTest")
    @Singleton
    @Replaces(QuestionRepository.class)
    static class QuestionRepositoryMock extends SecondaryQuestionRepository {

        Map<String, Question> questions = new HashMap<>();

        @Override
        public String save(Question question, Tenant tenant) {
            final String id = UUID.randomUUID().toString();
            questions.put(id, new QuestionRecord(id, question.title(), question.howOften(), question.days(), question.timeOfDay(), question.fixedTime(), question.respondents()));
            return id;
        }

        @Override
        public Optional<? extends Question> findById(String id, Tenant tenant) {
            return Optional.ofNullable(questions.get(id));
        }

        @Override
        public void update(Question questionUpdate, Tenant tenant) {
            questions.put(questionUpdate.id(), questionUpdate);
        }
    }

    @Requires(property = "spec.name", value = "QuestionServiceImplTest")
    @Singleton
    @Replaces(ProfileRepository.class)
    static class ProfileRepositoryMock extends SecondaryProfileRepository {

        @Override
        public Optional<? extends Profile> findById(String id, Tenant tenant) {
            throw new RuntimeException("Next execution time calculated");
        }
    }
}
