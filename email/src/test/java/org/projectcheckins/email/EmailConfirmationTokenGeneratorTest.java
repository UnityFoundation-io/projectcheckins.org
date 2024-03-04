package org.projectcheckins.email;

import io.micronaut.context.annotation.Property;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Property(name = "micronaut.security.token.jwt.signatures.secret.generator.secret", value="pleaseChangeThisSecretForANewOne")
@MicronautTest
class EmailConfirmationTokenGeneratorTest {

    @Test
    void testTokenGenerationAndValidation(EmailConfirmationTokenGenerator emailConfirmationTokenGenerator,
                                          EmailConfirmationTokenValidator emailConfirmationTokenValidator) {
        String email = "delamos@unityfoundation.io";
        String token = emailConfirmationTokenGenerator.generateToken(email);
        assertNotNull(token);
        Optional<Authentication> authenticationOptional = emailConfirmationTokenValidator.validate(token);
        assertTrue(authenticationOptional.isPresent());
        Authentication authentication = authenticationOptional.get();
        assertEquals(email, authentication.getName());
    }
}