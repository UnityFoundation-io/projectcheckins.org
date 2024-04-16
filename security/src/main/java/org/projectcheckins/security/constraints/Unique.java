package org.projectcheckins.security.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {

    String MESSAGE = "org.projectcheckins.security.constraints.Unique.message";

    String message() default "{" + MESSAGE + "}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

