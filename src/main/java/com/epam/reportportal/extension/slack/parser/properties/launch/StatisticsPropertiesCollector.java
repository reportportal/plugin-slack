package com.epam.reportportal.extension.slack.parser.properties.launch;

import static java.util.Optional.ofNullable;

import com.epam.reportportal.extension.slack.model.domain.template.property.TextProperty;
import com.epam.reportportal.extension.slack.parser.properties.PropertyCollector;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.launch.Launch;
import com.epam.ta.reportportal.entity.statistics.Statistics;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class StatisticsPropertiesCollector implements PropertyCollector<Launch, TextProperty> {

  @Override
  public Map<TextProperty, String> collect(Launch launch, Integration integration) {
    return ofNullable(launch.getStatistics()).filter(CollectionUtils::isNotEmpty)
        .map(statistics -> statistics.stream()
            .collect(Collectors.toMap(this::convertToTemplateProperty,
                s -> String.valueOf(s.getCounter()))))
        .orElseGet(Collections::emptyMap);
  }

  private TextProperty convertToTemplateProperty(Statistics statistics) {
    return new TextProperty(statistics.getStatisticsField().getName());
  }
}
