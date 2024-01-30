package com.epam.reportportal.extension.slack.event.launch.analysis;

import com.epam.reportportal.extension.event.LaunchStartUniqueErrorAnalysisEvent;
import com.epam.reportportal.extension.slack.event.handler.launch.LaunchEventHandler;
import org.springframework.context.ApplicationListener;

public class SlackUniqueErrorAnalysisStartEventListener implements
    ApplicationListener<LaunchStartUniqueErrorAnalysisEvent> {

  private final LaunchEventHandler<LaunchStartUniqueErrorAnalysisEvent> eventHandler;

  public SlackUniqueErrorAnalysisStartEventListener(
      LaunchEventHandler<LaunchStartUniqueErrorAnalysisEvent> eventHandler) {
    this.eventHandler = eventHandler;
  }

  @Override
  public void onApplicationEvent(LaunchStartUniqueErrorAnalysisEvent event) {
    eventHandler.handle(event);
  }
}
