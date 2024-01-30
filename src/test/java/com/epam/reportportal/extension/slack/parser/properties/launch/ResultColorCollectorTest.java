package com.epam.reportportal.extension.slack.parser.properties.launch;

import static com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty.RESULT_COLOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty;
import com.epam.reportportal.extension.slack.parser.properties.color.LaunchColorResolver;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.launch.Launch;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ResultColorCollectorTest {

  private final LaunchColorResolver launchColorResolver = mock(LaunchColorResolver.class);

  private final ResultColorCollector resultColorCollector = new ResultColorCollector(
      launchColorResolver);

  @Test
  void shouldCollect() {
    final Launch launch = new Launch();
    final Integration integration = new Integration();
    final String color = "color";

    when(launchColorResolver.resolve(launch)).thenReturn(color);

    final Map<DefaultTemplateProperty, String> properties = resultColorCollector.collect(launch,
        integration);

    assertEquals(color, properties.get(RESULT_COLOR));
  }

}