package com.epam.reportportal.extension.slack.event.launch.analysis;

import com.epam.reportportal.extension.event.LaunchUniqueErrorAnalysisFinishEvent;
import com.epam.reportportal.extension.slack.event.handler.launch.LaunchEventHandler;
import org.springframework.context.ApplicationListener;

public class SlackUniqueErrorAnalysisFinishEventListener implements
    ApplicationListener<LaunchUniqueErrorAnalysisFinishEvent> {

  private final LaunchEventHandler<LaunchUniqueErrorAnalysisFinishEvent> eventHandler;

  public SlackUniqueErrorAnalysisFinishEventListener(
      LaunchEventHandler<LaunchUniqueErrorAnalysisFinishEvent> eventHandler) {
    this.eventHandler = eventHandler;
  }

  @Override
  public void onApplicationEvent(LaunchUniqueErrorAnalysisFinishEvent event) {
    eventHandler.handle(event);
  }
}
