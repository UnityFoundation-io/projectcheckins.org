package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.beans.BeanProperty;
import io.micronaut.views.fields.messages.ConstraintViolationUtils;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.ConstraintViolationException;

import java.util.*;

public final class ConstraintViolationExceptionUtils {

    private ConstraintViolationExceptionUtils() {

    }

    public static Map<String, List<Message>> messageOf(@NonNull ConstraintViolationException ex, BeanIntrospection<?> beanIntrospection) {
        Map<String, List<Message>> errors = new HashMap<>();
        for (BeanProperty<?, ?> beanProperty : beanIntrospection.getBeanProperties()) {
            errors.put(beanProperty.getName(), messagesForConstraintViolationExceptionAndBeanProperty(ex, beanProperty));
        }
        return errors;
    }

    private static List<Message> messagesForConstraintViolationExceptionAndBeanProperty(@Nullable ConstraintViolationException ex,
                                                                                        @NonNull BeanProperty<?, ?> beanProperty) {
        return ex == null ? Collections.emptyList() : ex.getConstraintViolations().stream().filter((violation) -> {
                    Optional<String> lastNodeOptional = ConstraintViolationUtils.lastNode(violation);
                    return lastNodeOptional.isPresent() && (lastNodeOptional.get()).equals(beanProperty.getName());
                }).map(Message::of)
                .toList();
    }
}
