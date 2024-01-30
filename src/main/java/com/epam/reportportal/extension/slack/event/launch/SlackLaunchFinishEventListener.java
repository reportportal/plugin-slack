package com.epam.reportportal.extension.slack.event.launch;

import com.epam.reportportal.extension.event.LaunchFinishedPluginEvent;
import com.epam.reportportal.extension.slack.event.handler.launch.LaunchEventHandler;
import org.springframework.context.ApplicationListener;

public class SlackLaunchFinishEventListener implements
    ApplicationListener<LaunchFinishedPluginEvent> {

  private final LaunchEventHandler<LaunchFinishedPluginEvent> eventHandler;

  public SlackLaunchFinishEventListener(
      LaunchEventHandler<LaunchFinishedPluginEvent> eventHandler) {
    this.eventHandler = eventHandler;
  }

  @Override
  public void onApplicationEvent(LaunchFinishedPluginEvent event) {
    eventHandler.handle(event);
  }

}
