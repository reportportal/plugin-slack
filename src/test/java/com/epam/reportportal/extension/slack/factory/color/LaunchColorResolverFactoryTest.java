package com.epam.reportportal.extension.slack.factory.color;

import static com.epam.reportportal.extension.slack.model.enums.SlackEventType.AUTO_ANALYSIS_FINISHED;
import static com.epam.reportportal.extension.slack.model.enums.SlackEventType.LAUNCH_FINISHED;
import static com.epam.reportportal.extension.slack.model.enums.SlackEventType.LAUNCH_STARTED;
import static com.epam.reportportal.extension.slack.model.enums.SlackEventType.UNIQUE_ERROR_ANALYSIS_FINISHED;
import static com.epam.reportportal.extension.slack.model.enums.SlackEventType.UNIQUE_ERROR_ANALYSIS_STARTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import com.epam.reportportal.extension.slack.parser.properties.color.LaunchStatusColorResolver;
import com.epam.reportportal.extension.slack.parser.properties.color.StaticColorResolver;
import java.util.Arrays;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LaunchColorResolverFactoryTest {

  private final LaunchColorResolverFactory factory = new LaunchColorResolverFactory();

  private final Set<SlackEventType> staticColorTypes = Set.of(LAUNCH_STARTED,
      AUTO_ANALYSIS_FINISHED, UNIQUE_ERROR_ANALYSIS_STARTED, UNIQUE_ERROR_ANALYSIS_FINISHED);
  private final Set<SlackEventType> launchStatusColorTypes = Set.of(LAUNCH_FINISHED);

  @Test
  void shouldGetResolverForEachEvent() {
    Arrays.stream(SlackEventType.values())
        .forEach(eventType -> assertNotNull(factory.get(eventType)));
  }

  @Test
  void shouldGetValidColorResolver() {
    staticColorTypes.stream().map(factory::get).forEach(resolver -> assertEquals(
        StaticColorResolver.class, resolver.getClass()));
    launchStatusColorTypes.stream().map(factory::get).forEach(resolver -> assertEquals(
        LaunchStatusColorResolver.class, resolver.getClass()));
  }

}