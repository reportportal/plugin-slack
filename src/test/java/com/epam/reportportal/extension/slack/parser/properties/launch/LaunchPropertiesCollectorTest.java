package com.epam.reportportal.extension.slack.parser.properties.launch;

import static com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty.LAUNCH_DESCRIPTION;
import static com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty.LAUNCH_FINISH_TIME;
import static com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty.LAUNCH_ID;
import static com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty.LAUNCH_MODE;
import static com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty.LAUNCH_NAME;
import static com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty.LAUNCH_NUMBER;
import static com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty.LAUNCH_START_TIME;
import static com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty.LAUNCH_UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty;
import com.epam.ta.reportportal.entity.enums.LaunchModeEnum;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.launch.Launch;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.Test;

class LaunchPropertiesCollectorTest {

  private final LaunchPropertiesCollector launchPropertiesCollector = new LaunchPropertiesCollector();

  @Test
  void shouldCollectLaunchProperties() {
    final Launch launch = new Launch();
    launch.setId(1L);
    launch.setUuid("uuid");
    launch.setName("my name");
    launch.setNumber(11L);
    final LocalDateTime startTime = LocalDateTime.now();
    launch.setStartTime(startTime);
    final LocalDateTime endTime = startTime.plusMinutes(1L);
    launch.setEndTime(endTime);
    launch.setMode(LaunchModeEnum.DEFAULT);
    launch.setDescription("my description");

    final Map<DefaultTemplateProperty, String> launchProperties = launchPropertiesCollector.collect(launch,
        new Integration());

    assertEquals(String.valueOf(launch.getId()), launchProperties.get(LAUNCH_ID));
    assertEquals(launch.getUuid(), launchProperties.get(LAUNCH_UUID));
    assertEquals(launch.getName(), launchProperties.get(LAUNCH_NAME));
    assertEquals(String.valueOf(launch.getNumber()), launchProperties.get(LAUNCH_NUMBER));
    assertEquals(String.valueOf(launch.getStartTime()), launchProperties.get(LAUNCH_START_TIME));
    assertEquals(String.valueOf(launch.getEndTime()), launchProperties.get(LAUNCH_FINISH_TIME));
    assertEquals(String.valueOf(launch.getMode()), launchProperties.get(LAUNCH_MODE));
    assertEquals(launch.getDescription(), launchProperties.get(LAUNCH_DESCRIPTION));

  }

}