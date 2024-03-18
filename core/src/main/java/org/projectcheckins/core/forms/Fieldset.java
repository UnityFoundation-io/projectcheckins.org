package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.views.fields.messages.Message;

import java.util.List;
import java.util.Map;

public interface Fieldset {
    @Nullable Map<String, List<Message>> fieldErrors();
    @Nullable List<Message> errors();
}
