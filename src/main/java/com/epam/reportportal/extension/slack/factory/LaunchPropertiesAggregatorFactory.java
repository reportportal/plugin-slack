package com.epam.reportportal.extension.slack.factory;

import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import com.epam.reportportal.extension.slack.parser.properties.PropertyCollector;
import com.epam.reportportal.extension.slack.parser.properties.aggregator.LaunchPropertiesAggregator;
import com.epam.ta.reportportal.entity.launch.Launch;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LaunchPropertiesAggregatorFactory {

  private final PropertyCollectorFactory propertyCollectorFactory;

  private final Map<SlackEventType, LaunchPropertiesAggregator> mapping;

  public LaunchPropertiesAggregatorFactory(PropertyCollectorFactory propertyCollectorFactory) {
    this.propertyCollectorFactory = propertyCollectorFactory;
    this.mapping = initMapping();
  }

  public LaunchPropertiesAggregator get(SlackEventType eventType) {
    return mapping.get(eventType);
  }

  private Map<SlackEventType, LaunchPropertiesAggregator> initMapping() {
    return Arrays.stream(SlackEventType.values())
        .collect(Collectors.toMap(eventType -> eventType, this::getLaunchPropertiesAggregator));
  }

  private LaunchPropertiesAggregator getLaunchPropertiesAggregator(
      SlackEventType eventType) {
    final List<PropertyCollector<Launch, ? extends TemplateProperty>> propertyCollectors = new ArrayList<>(
        propertyCollectorFactory.getDefaultCollectors());
    propertyCollectors.addAll(propertyCollectorFactory.get(eventType));
    return new LaunchPropertiesAggregator(propertyCollectors);
  }

}
