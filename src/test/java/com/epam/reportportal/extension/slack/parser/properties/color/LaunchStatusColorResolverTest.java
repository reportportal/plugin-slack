package com.epam.reportportal.extension.slack.parser.properties.color;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.epam.ta.reportportal.entity.enums.StatusEnum;
import com.epam.ta.reportportal.entity.launch.Launch;
import org.junit.jupiter.api.Test;

class LaunchStatusColorResolverTest {

  private final String passedColor = "green";
  private final String failedColor = "red";

  private final LaunchStatusColorResolver launchStatusColorResolver = new LaunchStatusColorResolver(
      passedColor, failedColor);

  @Test
  void shouldPassedColorWhenLaunchStatusPassed() {
    final Launch launch = new Launch();
    launch.setStatus(StatusEnum.PASSED);

    final String resolvedColor = launchStatusColorResolver.resolve(launch);

    assertEquals(passedColor, resolvedColor);
  }

  @Test
  void shouldFailedColorWhenLaunchStatusNotPassed() {
    final Launch launch = new Launch();
    launch.setStatus(StatusEnum.STOPPED);

    final String resolvedColor = launchStatusColorResolver.resolve(launch);

    assertEquals(failedColor, resolvedColor);
  }

}