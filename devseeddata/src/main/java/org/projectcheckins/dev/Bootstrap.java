package org.projectcheckins.dev;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.ApplicationEventListener;

import io.micronaut.runtime.server.event.ServerStartupEvent;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import org.projectcheckins.core.exceptions.UserNotFoundException;
import org.projectcheckins.core.forms.AnswerSave;
import org.projectcheckins.core.forms.Format;
import org.projectcheckins.core.forms.Profile;
import org.projectcheckins.core.forms.QuestionSave;
import org.projectcheckins.core.repositories.AnswerRepository;
import org.projectcheckins.core.repositories.ProfileRepository;
import org.projectcheckins.core.repositories.QuestionRepository;
import org.projectcheckins.email.EmailConfirmationRepository;
import org.projectcheckins.security.RegisterService;
import org.projectcheckins.security.UserAlreadyExistsException;
import org.projectcheckins.annotations.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

@Generated// "ignore for jacoco"
@Requires(env = Environment.DEVELOPMENT)
@Singleton
public class Bootstrap implements ApplicationEventListener<ServerStartupEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);

    private final RegisterService registerService;
    private final EmailConfirmationRepository emailConfirmationRepository;
    private final ProfileRepository profileRepository;
    private final AnswerRepository answerRepository;

    private final QuestionRepository questionRepository;

    public Bootstrap(RegisterService registerService,
                     EmailConfirmationRepository emailConfirmationRepository,
                     ProfileRepository profileRepository,
                     AnswerRepository answerRepository,
                     QuestionRepository questionRepository) {
        this.registerService = registerService;
        this.emailConfirmationRepository = emailConfirmationRepository;
        this.profileRepository = profileRepository;
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        String sergioId = addUser("delamos@unityfoundation.io");

        String questionWhatAreYouWorkingOnThisWeekId = questionRepository.save(new QuestionSave("What are you working on this week?"));

        Authentication sergio = Authentication.build(sergioId);
        answerRepository.save(new AnswerSave(questionWhatAreYouWorkingOnThisWeekId,
        Format.MARKDOWN,
                LocalDate.of(2024, 3, 11), """
                        - 4.4.0 core release and building every module with 4.4.0 core.
                        - HTMX integration
                        - Project checkins
                        """
                ), sergio);

        answerRepository.save(new AnswerSave(questionWhatAreYouWorkingOnThisWeekId,
                Format.MARKDOWN,
                LocalDate.of(2024, 2, 26), """
                        - Project Checkins Kickoff
                        - Micronaut Framework 5 planning
                        - 4.3.4 release.
                        """
        ), sergio);

        answerRepository.save(new AnswerSave(questionWhatAreYouWorkingOnThisWeekId,
                Format.MARKDOWN,
                LocalDate.of(2024, 2, 19), """
                        - 4.3.3. release
                        - Youtube channel
                        - GitHub notifications
                        """
        ), sergio);

        answerRepository.save(new AnswerSave(questionWhatAreYouWorkingOnThisWeekId,
                Format.MARKDOWN,
                LocalDate.of(2024, 2, 12), """
                        4.4.0 and 5.0.0 planning.
                        """
        ), sergio);

        answerRepository.save(new AnswerSave(questionWhatAreYouWorkingOnThisWeekId,
                Format.MARKDOWN,
                LocalDate.of(2024, 2, 5), """
                        - 4.3.0 release 4.4.0 and 5.0.0 planning
                        """
        ), sergio);


        String guillermoId = addUser("calvog@unityfoundation.io");
        Authentication guillermo = Authentication.build(guillermoId);
        String jeremyId = addUser("grellej@unityfoundation.io");
        Authentication jeremy = Authentication.build(jeremyId);
        String timId = addUser("yatest@unityfoundation.io");
        Authentication tim = Authentication.build(timId);
        String deanId = addUser("wetted@objectcomputing.com");
        Authentication dean = Authentication.build(deanId);
    }

    private String addUser(String email) {
        try {
            registerService.register(email, "secret");
        } catch (UserAlreadyExistsException e) {
            LOG.info("user {} already exists", email);
        }
        emailConfirmationRepository.enableByEmail(email);
        return profileRepository.findByEmail(email).map(Profile::id).orElseThrow(UserNotFoundException::new);
    }
}