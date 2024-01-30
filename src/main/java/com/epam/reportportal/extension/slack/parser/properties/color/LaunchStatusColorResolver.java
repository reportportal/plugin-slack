package com.epam.reportportal.extension.slack.parser.properties.color;

import static com.epam.ta.reportportal.entity.enums.StatusEnum.PASSED;

import com.epam.ta.reportportal.entity.launch.Launch;

public class LaunchStatusColorResolver implements LaunchColorResolver {

  private final String passedColor;
  private final String notPassedColor;

  public LaunchStatusColorResolver(String passedColor, String notPassedColor) {
    this.passedColor = passedColor;
    this.notPassedColor = notPassedColor;
  }

  //TODO can be changed to have a Map<Status, Color>
  @Override
  public String resolve(Launch launch) {
    return PASSED == launch.getStatus() ? passedColor : notPassedColor;
  }
}
