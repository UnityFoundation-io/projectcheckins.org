package org.projectcheckins.security.forms;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.projectcheckins.test.ValidationAssert.assertThat;

@MicronautTest(startApplication = false)
class TeamMemberUninviteTest {

    final static Argument<TeamMemberUninvite> ARGUMENT = Argument.of(TeamMemberUninvite.class);

    @Test
    void emailIsRequired(Validator validator) {
        assertThat(validator.validate(new TeamMemberUninvite(null)))
                .hasNotBlankViolation("email");
        assertThat(validator.validate(new TeamMemberUninvite("")))
                .hasNotBlankViolation("email");
        assertThat(validator.validate(new TeamMemberUninvite("example@projectcheckins.org")))
                .isValid();
    }

    @Test
    void teamMemberUninviteIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serde) {
        assertThatCode(() -> serde.getDeserializableIntrospection(ARGUMENT))
                .doesNotThrowAnyException();
    }

    @Test
    void teamMemberUninviteIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serde) {
        assertThatCode(() -> serde.getSerializableIntrospection(ARGUMENT))
                .doesNotThrowAnyException();
    }
}
