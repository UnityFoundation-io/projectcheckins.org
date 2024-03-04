package org.projectcheckins.email;

import io.micronaut.context.annotation.Property;
import io.micronaut.email.BodyType;
import io.micronaut.email.Email;
import io.micronaut.email.MultipartBody;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@Property(name = "email.sender", value = "info@projectcheckins.org")
@MicronautTest
class EmailConfirmationComposerTest {

    @Test
    void emailConfirmation(EmailConfirmationComposer emailConfirmationComposer) {
        Email.Builder emailBuilder = emailConfirmationComposer.composeEmailConfirmation(Locale.ENGLISH,
                new EmailConfirmation("https://projectcheckins.example.com/email/confirm", "sergio.delamo@softamo.com"));
        assertNotNull(emailBuilder);
        Email email = emailBuilder.build();
        assertEquals("Confirm email address for your Project Check-ins account", email.getSubject());
        assertEquals("info@projectcheckins.org", email.getFrom().getEmail());
        assertEquals(1, email.getTo().size());
        assertEquals("sergio.delamo@softamo.com", email.getTo().stream().findFirst().get().getEmail());
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

    }
}