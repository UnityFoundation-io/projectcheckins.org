package org.projectcheckins.bootstrap;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false)
class BreadcrumbTest {

    @Test
    void labelNotNull(Validator validator) {
        ValidationAssert.assertThat(validator, new Breadcrumb(null, null))
                .fieldNotNull("label");
    }

    @Test
    void validBreadcrumb(Validator validator) {
        ValidationAssert.assertThat(validator, new Breadcrumb(Message.of("Hello World")))
                .isValid();
    }
}