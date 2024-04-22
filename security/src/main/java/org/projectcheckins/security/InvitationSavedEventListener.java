package org.projectcheckins.security;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.core.util.locale.LocaleResolutionConfiguration;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

@Requires(beans = {EmailInvitationSender.class})
@Singleton
class InvitationSavedEventListener implements ApplicationEventListener<InvitationSavedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(InvitationSavedEventListener.class);

    private final EmailInvitationSender emailInvitationSender;
    private final Locale locale;

    InvitationSavedEventListener(EmailInvitationSender emailInvitationSender,
                                 LocaleResolutionConfiguration localeResolutionConfig) {
        this.emailInvitationSender = emailInvitationSender;
        this.locale = localeResolutionConfig.getFixed().orElseGet(localeResolutionConfig::getDefaultLocale);
    }

    @Override
    public void onApplicationEvent(InvitationSavedEvent event) {
        LOG.trace("Received InvitationSavedEvent with email {} and url {} - {}", event.getEmail(), event.getUrl(), event.getSource());
        emailInvitationSender.sendInvitationEmail(event.getEmail(), event.getUrl(), locale);
    }
}
