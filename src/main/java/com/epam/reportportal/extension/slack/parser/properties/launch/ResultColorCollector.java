package com.epam.reportportal.extension.slack.parser.properties.launch;

import static com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty.RESULT_COLOR;

import com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty;
import com.epam.reportportal.extension.slack.parser.properties.PropertyCollector;
import com.epam.reportportal.extension.slack.parser.properties.color.ColorResolver;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.launch.Launch;
import java.util.Map;

public class ResultColorCollector implements PropertyCollector<Launch, DefaultTemplateProperty> {

  private final ColorResolver<Launch> resolver;

  public ResultColorCollector(ColorResolver<Launch> resolver) {
    this.resolver = resolver;
  }

  @Override
  public Map<DefaultTemplateProperty, String> collect(Launch launch, Integration integration) {
    return Map.of(RESULT_COLOR, resolver.resolve(launch));
  }
}
