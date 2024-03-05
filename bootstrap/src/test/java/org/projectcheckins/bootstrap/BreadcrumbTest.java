package org.projectcheckins.bootstrap;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import static org.projectcheckins.bootstrap.AssertUtils.*;

@MicronautTest(startApplication = false)
class BreadcrumbTest {

    @Test
    void labelNotNull(Validator validator) {
        assertNotNull(validator, new Breadcrumb(null, null), "label");
    }

}