package com.epam.reportportal.extension.slack.event.launch;

import com.epam.reportportal.extension.event.StartLaunchEvent;
import com.epam.reportportal.extension.slack.event.handler.launch.LaunchEventHandler;
import org.springframework.context.ApplicationListener;

public class SlackLaunchStartEventListener implements ApplicationListener<StartLaunchEvent> {

  private final LaunchEventHandler<StartLaunchEvent> eventHandler;

  public SlackLaunchStartEventListener(
      LaunchEventHandler<StartLaunchEvent> eventHandler) {
    this.eventHandler = eventHandler;
  }

  @Override
  public void onApplicationEvent(StartLaunchEvent event) {
    eventHandler.handle(event);
  }
}
