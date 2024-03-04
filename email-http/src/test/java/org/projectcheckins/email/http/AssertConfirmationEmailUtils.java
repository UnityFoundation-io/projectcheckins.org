package org.projectcheckins.email.http;

import io.micronaut.email.BodyType;
import io.micronaut.email.Email;
import io.micronaut.email.MultipartBody;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class AssertConfirmationEmailUtils {
    private AssertConfirmationEmailUtils() {

    }

    public static void assertConfirmationEmail(String recipient, Email email) {
        assertThat(email.getSubject())
                .isEqualTo("Confirm email address for your Project Check-ins account");
        assertThat(email.getFrom().getEmail())
                .isEqualTo("info@projectcheckins.org");
        assertThat(email.getTo())
                .hasSize(1);
        assertEquals(recipient, email.getTo().stream().findFirst().get().getEmail());

        assertThat(email.getBody())
                .isInstanceOf(MultipartBody.class);
        MultipartBody multipartBody = (MultipartBody) email.getBody();
        assertThat(multipartBody.get(BodyType.TEXT))
                .isNotEmpty();

        assertTrue(multipartBody.get(BodyType.TEXT).get().startsWith("""
                To confirm your email address, please click the link below:
                https://projectcheckins.example.com/email/confirm?token="""));
        assertFalse(multipartBody.get(BodyType.HTML).isEmpty());
        assertTrue(multipartBody.get(BodyType.HTML).get().startsWith("""
                To confirm your email address, please click the link below:
                <a href="https://projectcheckins.example.com/email/confirm?token="""));
    }
}
