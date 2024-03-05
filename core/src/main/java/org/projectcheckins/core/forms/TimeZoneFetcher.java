package org.projectcheckins.core.forms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.views.fields.elements.Option;
import io.micronaut.views.fields.fetchers.OptionFetcher;
import io.micronaut.views.fields.messages.Message;
import jakarta.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

@Singleton
public class TimeZoneFetcher implements OptionFetcher<String> {

  @Override
  public List<Option> generate(@NonNull Class<String> type) {
    return generate((String) null);
  }

  @Override
  public List<Option> generate(@NonNull String instance) {
    return Arrays.stream(TimeZone.getAvailableIDs())
        .map(TimeZone::getTimeZone)
        .map(tz -> option(tz, instance))
        .toList();
  }

  private Option option(TimeZone instance, String selected) {
    return Option.builder()
        .value(instance.getID())
        .label(Message.of(getLabel(instance)))
        .selected(instance.getID().equals(selected))
        .build();
  }

  private static String getLabel(TimeZone timeZone) {
    return timeZone.getID() + " (" + timeZone.getDisplayName() + ")";
  }
}
