package org.projectcheckins.email.http;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.*;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
@Property(name = "spec.name", value = "EmailConfirmationSenderTest")
@Property(name = "email.sender", value = "info@projectcheckins.org")
@Property(name = "micronaut.security.token.jwt.signatures.secret.generator.secret", value="pleaseChangeThisSecretForANewOne")
class EmailConfirmationSenderTest {

    @Test
    void testSendConfirmationEmail(EmailConfirmationSender emailConfirmationSender, EmailSenderReplacement<?> emailSenderReplacement) {
        String recipient = "delamos@unityfoundation.io";
        emailConfirmationSender.sendConfirmationEmail(recipient, "https://projectcheckins.example.com", Locale.ENGLISH);
        await().atMost(1, SECONDS).until(() -> !emailSenderReplacement.getEmails().isEmpty());
        List<Email> emails = emailSenderReplacement.getEmails();
        assertEquals(1, emails.size());
        Email email = emails.getFirst();

        assertThat(email.getSubject())
                .isEqualTo("Confirm email address for your Project Check-ins account");
        assertThat(email.getFrom().getEmail())
                .isEqualTo("info@projectcheckins.org");
        assertThat(email.getTo())
                .hasSize(1);
        assertEquals(recipient, email.getTo().stream().findFirst().get().getEmail());

        assertTrue(email.getBody() instanceof MultipartBody);
        MultipartBody multipartBody = (MultipartBody) email.getBody();
        assertFalse(multipartBody.get(BodyType.TEXT).isEmpty());

        assertTrue(multipartBody.get(BodyType.TEXT).get().startsWith("""
                To confirm your email address, please click the link below:
                https://projectcheckins.example.com/email/confirm?token="""));
        assertFalse(multipartBody.get(BodyType.HTML).isEmpty());
        assertTrue(multipartBody.get(BodyType.HTML).get().startsWith("""
                To confirm your email address, please click the link below:
                <a href="https://projectcheckins.example.com/email/confirm?token="""));

        emailSenderReplacement.getEmails().clear();
        try {
            emailConfirmationSender.sendConfirmationEmail(recipient);
            Thread.sleep(1000); // waits for 1 second
            assertTrue(emailSenderReplacement.getEmails().isEmpty());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Requires(property = "spec.name", value = "EmailConfirmationSenderTest")
    @Singleton
    @Named(EmailSenderReplacement.NAME)
    @Replaces(EmailSender.class)
    static class EmailSenderReplacement<I> implements EmailSender<I, Boolean> {
        private static final String NAME = "EmailSenderReplacement";
        private List<Email> emails = new ArrayList<>();

        @Override
        public @NonNull Boolean send(Email.Builder emailBuilder, @NonNull @NotNull Consumer<I> emailRequest) throws EmailException {
            emails.add(emailBuilder.build());
            return Boolean.TRUE;
        }

        @Override
        public @NonNull String getName() {
            return NAME;
        }

        public List<Email> getEmails() {
            return emails;
        }
    }
}