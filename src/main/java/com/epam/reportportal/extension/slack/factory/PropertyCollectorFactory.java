package com.epam.reportportal.extension.slack.factory;

import com.epam.reportportal.extension.slack.factory.color.ColorResolverFactory;
import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import com.epam.reportportal.extension.slack.parser.properties.PropertyCollector;
import com.epam.reportportal.extension.slack.parser.properties.launch.AttributesCollector;
import com.epam.reportportal.extension.slack.parser.properties.launch.LaunchLinkCollector;
import com.epam.reportportal.extension.slack.parser.properties.launch.LaunchPropertiesCollector;
import com.epam.reportportal.extension.slack.parser.properties.launch.ResultColorCollector;
import com.epam.reportportal.extension.slack.parser.properties.launch.StatisticsPropertiesCollector;
import com.epam.ta.reportportal.dao.ProjectRepository;
import com.epam.ta.reportportal.entity.launch.Launch;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PropertyCollectorFactory {

  private final ColorResolverFactory<Launch> colorResolverFactory;

  private final List<PropertyCollector<Launch, ? extends TemplateProperty>> defaultPropertyCollectors;
  private final Map<SlackEventType, List<PropertyCollector<Launch, ? extends TemplateProperty>>> mapping;

  public PropertyCollectorFactory(
      ColorResolverFactory<Launch> colorResolverFactory,
      ProjectRepository projectRepository) {
    this.colorResolverFactory = colorResolverFactory;
    defaultPropertyCollectors = List.of(
        new LaunchLinkCollector(projectRepository),
        new LaunchPropertiesCollector(),
        new AttributesCollector(),
        new StatisticsPropertiesCollector());
    mapping = initMapping();
  }

  public List<PropertyCollector<Launch, ? extends TemplateProperty>> getDefaultCollectors() {
    return defaultPropertyCollectors;
  }

  public List<PropertyCollector<Launch, ? extends TemplateProperty>> get(SlackEventType eventType) {
    return mapping.get(eventType);
  }

  private Map<SlackEventType, List<PropertyCollector<Launch, ? extends TemplateProperty>>> initMapping() {
    return Arrays.stream(SlackEventType.values())
        .collect(Collectors.toMap(eventType -> eventType, this::getResultColorCollectors));
  }

  private List<PropertyCollector<Launch, ? extends TemplateProperty>> getResultColorCollectors(
      SlackEventType eventType) {
    return List.of(new ResultColorCollector(colorResolverFactory.get(eventType)));
  }
}
