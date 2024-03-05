package org.projectcheckins.core.models;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.projectcheckins.core.AssertUtils.assertNotBlank;

@MicronautTest(startApplication = false)
class ElementTest {
    @Test
    void elementIdIsRequired(Validator validator) {
        assertNotBlank(validator, new Element("", "What are you working on?"), "id");
        assertNotBlank(validator, new Element(null, "What are you working on?"), "id");
    }

    @Test
    void elementNameIsRequired(Validator validator) {
        assertNotBlank(validator, new Element("xxx", ""), "name");
        assertNotBlank(validator, new Element("xxx", null), "name");
    }

    @Test
    void elementIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(Element.class)))
                .doesNotThrowAnyException();
    }

    @Test
    void elementIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(Element.class)))
                .doesNotThrowAnyException();
    }

}