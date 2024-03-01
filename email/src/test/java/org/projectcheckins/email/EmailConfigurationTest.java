package org.projectcheckins.email;

import io.micronaut.context.annotation.Property;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Property(name = "email.sender", value = "info@projectcheckins.org")
@MicronautTest
class EmailConfigurationTest {

    @Test
    void testEmailConfiguration(EmailConfiguration emailConfiguration) {
        assertEquals("info@projectcheckins.org", emailConfiguration.getSender());
    }
}
