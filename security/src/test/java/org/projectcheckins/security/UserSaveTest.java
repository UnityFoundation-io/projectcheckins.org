package org.projectcheckins.security;

import static java.util.Collections.emptyList;
import static java.util.TimeZone.getDefault;
import static java.time.DayOfWeek.MONDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false)
class UserSaveTest {
    @Test
    void usernameIsRequired(Validator validator) {
        assertThat(validator.validate(new UserSave(null, "encodedpassword", emptyList(), getDefault(), MONDAY, true)))
            .isNotEmpty();
        assertThat(validator.validate(new UserSave("", "encodedpassword", emptyList(), getDefault(), MONDAY, true)))
            .anyMatch(x -> x.getPropertyPath().toString().equals("email") && x.getMessage().equals("must not be blank"));
        assertThat(validator.validate(new UserSave("manolo", "encodedpassword", emptyList(), getDefault(), MONDAY, true)))
            .isEmpty();
    }

    @Test
    void passwordIsRequired(Validator validator) {
        assertThat(validator.validate(new UserSave("manolo", null, emptyList(), getDefault(), MONDAY, true)))
            .isNotEmpty();
        assertThat(validator.validate(new UserSave("manolo", "", emptyList(), getDefault(), MONDAY, true)))
            .anyMatch(x -> x.getPropertyPath().toString().equals("encodedPassword") && x.getMessage().equals("must not be blank"));
    }

    @Test
    void timeZoneIsRequired(Validator validator) {
        assertThat(validator.validate(new UserSave("manolo", null, emptyList(), null, MONDAY, true)))
            .isNotEmpty();
    }

    @Test
    void firstDayOfWeekIsRequired(Validator validator) {
        assertThat(validator.validate(new UserSave("manolo", null, emptyList(), getDefault(), null, true)))
            .isNotEmpty();
    }

    @Test
    void userSaveIsAnnotatedWithSerdeableDeserializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(UserSave.class)))
            .doesNotThrowAnyException();
    }

    @Test
    void userSaveIsAnnotatedWithSerdeableSerializable(SerdeIntrospections serdeIntrospections) {
        assertThatCode(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(UserSave.class)))
            .doesNotThrowAnyException();
    }

}