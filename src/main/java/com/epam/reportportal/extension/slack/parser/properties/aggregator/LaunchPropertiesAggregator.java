package com.epam.reportportal.extension.slack.parser.properties.aggregator;

import static java.util.Optional.ofNullable;

import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import com.epam.reportportal.extension.slack.parser.properties.PropertyCollector;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.launch.Launch;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LaunchPropertiesAggregator {

  private final List<PropertyCollector<Launch, ? extends TemplateProperty>> propertyCollectors;

  public LaunchPropertiesAggregator(
      List<PropertyCollector<Launch, ? extends TemplateProperty>> propertyCollectors) {
    this.propertyCollectors = propertyCollectors;
  }

  public Map<TemplateProperty, String> aggregate(Launch launch, Integration integration) {
    return propertyCollectors.stream()
        .map(c -> c.collect(launch, integration))
        .flatMap(m -> m.entrySet().stream())
        .collect(
            LinkedHashMap::new,
            (m, v) -> m.put(v.getKey(),
                ofNullable(v.getValue()).map(String::valueOf).orElse("")),
            LinkedHashMap::putAll
        );
  }
}
