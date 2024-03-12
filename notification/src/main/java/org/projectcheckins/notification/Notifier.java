package org.projectcheckins.notification;

import org.projectcheckins.core.forms.Profile;
import org.projectcheckins.core.forms.Question;

/**
 * Sends notifications about questions to specific user profiles.
 */
public interface Notifier {

    /**
     * Sends a notifications about a question to a user profile.
     *
     * @param question The question.
     * @param profile  The user profile.
     */
    void notify(Question question, Profile profile);
}
