package com.epam.reportportal.extension.slack.parser.properties.launch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.reportportal.extension.slack.model.domain.template.property.TextProperty;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.launch.Launch;
import com.epam.ta.reportportal.entity.statistics.Statistics;
import com.epam.ta.reportportal.entity.statistics.StatisticsField;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class StatisticsPropertiesCollectorTest {

  private final StatisticsPropertiesCollector statisticsPropertiesCollector = new StatisticsPropertiesCollector();

  @Test
  void shouldCollectEmptyIfStatisticsIsNull() {
    final Map<TextProperty, String> properties = statisticsPropertiesCollector.collect(
        new Launch(), new Integration());

    assertTrue(properties.isEmpty());
  }

  @Test
  void shouldCollectEmptyIfStatisticsIsEmpty() {
    final Launch launch = new Launch();
    launch.setStatistics(Collections.emptySet());

    final Map<TextProperty, String> properties = statisticsPropertiesCollector.collect(launch,
        new Integration());

    assertTrue(properties.isEmpty());
  }

  @Test
  void shouldCollect() {

    final Set<String> statisticsKeys = Sets.newHashSet(
        "statistics$executions$total",
        "statistics$executions$passed",
        "statistics$executions$failed",
        "statistics$executions$skipped",
        "statistics$defects$automation_bug$AB001",
        "statistics$defects$product_bug$PB001",
        "statistics$defects$to_investigate$TI001",
        "statistics$defects$system_issue$SI001",
        "statistics$defects$no_defect$ND001",
        "statistics$defects$no_defect$total",
        "statistics$defects$product_bug$total",
        "statistics$defects$to_investigate$total",
        "statistics$defects$system_issue$total"
    );

    final Random random = new Random();

    final Set<Statistics> launchStatistics = statisticsKeys.stream()
        .map(s -> new Statistics(new StatisticsField(s), random.nextInt(10))).collect(
            Collectors.toSet());

    final Launch launch = new Launch();
    launch.setStatistics(launchStatistics);

    final Map<TextProperty, String> properties = statisticsPropertiesCollector.collect(launch,
        new Integration());

    assertFalse(properties.isEmpty());

    assertEquals(launch.getStatistics().size(), properties.size());

    launch.getStatistics().forEach(s -> assertEquals(String.valueOf(s.getCounter()),
        properties.get(new TextProperty(s.getStatisticsField().getName()))));
  }
}