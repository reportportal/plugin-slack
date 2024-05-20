package com.epam.reportportal.extension.slack.factory;

import com.epam.reportportal.extension.slack.collector.PropertyCollector;
import com.epam.reportportal.extension.slack.collector.laucnh.AttributesCollector;
import com.epam.reportportal.extension.slack.collector.laucnh.LaunchPropertiesCollector;
import com.epam.reportportal.extension.slack.collector.laucnh.ResultColorCollector;
import com.epam.reportportal.extension.slack.collector.laucnh.StatisticsPropertiesCollector;
import com.epam.reportportal.extension.slack.model.template.TemplateProperty;
import com.epam.ta.reportportal.entity.launch.Launch;
import java.util.List;

public class PropertyCollectorFactory {

  private final List<PropertyCollector<Launch, ? extends TemplateProperty>> defaultPropertyCollectors;

  public PropertyCollectorFactory() {
    defaultPropertyCollectors = List.of(
        new LaunchPropertiesCollector(),
        new AttributesCollector(),
        new StatisticsPropertiesCollector(),
        new ResultColorCollector());
  }

  public List<PropertyCollector<Launch, ? extends TemplateProperty>> getDefaultCollectors() {
    return defaultPropertyCollectors;
  }

}
