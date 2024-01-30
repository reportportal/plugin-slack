package com.epam.reportportal.extension.slack.factory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class LaunchPropertiesAggregatorFactoryTest {

  private final PropertyCollectorFactory propertyCollectorFactory = mock(
      PropertyCollectorFactory.class);

  private final LaunchPropertiesAggregatorFactory factory = new LaunchPropertiesAggregatorFactory(
      propertyCollectorFactory);

  @Test
  void shouldGetPropertiesAggregatorForEachEvent() {
    Arrays.stream(SlackEventType.values())
        .forEach(eventType -> assertNotNull(factory.get(eventType)));
  }
}