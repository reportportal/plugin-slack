package com.epam.reportportal.extension.slack.parser.properties.aggregator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import com.epam.reportportal.extension.slack.model.domain.template.property.TextProperty;
import com.epam.reportportal.extension.slack.parser.properties.PropertyCollector;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.launch.Launch;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class LaunchPropertiesAggregatorTest {

  private final PropertyCollector<Launch, TemplateProperty> firstCollector = mock(
      PropertyCollector.class);
  private final PropertyCollector<Launch, TemplateProperty> secondCollector = mock(
      PropertyCollector.class);

  private final LaunchPropertiesAggregator launchPropertiesAggregator = new LaunchPropertiesAggregator(
      List.of(firstCollector, secondCollector));

  @Test
  void shouldInvokeEachCollectorOnce() {
    final Launch launch = new Launch();
    final Integration integration = new Integration();

    final Map<TemplateProperty, String> firstCollectorProperties = Map.of(
        new TextProperty("firstCollector"), "firstValue");
    final Map<TemplateProperty, String> secondCollectorProperties = Map.of(
        new TextProperty("secondCollector"),
        "secondValue");

    when(firstCollector.collect(launch, integration)).thenReturn(firstCollectorProperties);
    when(secondCollector.collect(launch, integration)).thenReturn(
        secondCollectorProperties);

    final Map<TemplateProperty, String> launchProperties = launchPropertiesAggregator.aggregate(
        launch,
        integration);

    verify(firstCollector, times(1)).collect(launch, integration);
    verify(secondCollector, times(1)).collect(launch, integration);

    assertEquals(launchProperties.size(),
        firstCollectorProperties.size() + secondCollectorProperties.size());

    firstCollectorProperties.forEach((k, v) -> assertEquals(v, launchProperties.get(k)));
    secondCollectorProperties.forEach((k, v) -> assertEquals(v, launchProperties.get(k)));
  }

}