package com.epam.reportportal.extension.slack.event.launch.analysis;

import com.epam.reportportal.extension.event.LaunchAutoAnalysisFinishEvent;
import com.epam.reportportal.extension.slack.event.handler.launch.LaunchEventHandler;
import org.springframework.context.ApplicationListener;

public class SlackAutoAnalysisFinishEventListener implements
    ApplicationListener<LaunchAutoAnalysisFinishEvent> {

  private final LaunchEventHandler<LaunchAutoAnalysisFinishEvent> eventHandler;

  public SlackAutoAnalysisFinishEventListener(
      LaunchEventHandler<LaunchAutoAnalysisFinishEvent> eventHandler) {
    this.eventHandler = eventHandler;
  }

  @Override
  public void onApplicationEvent(LaunchAutoAnalysisFinishEvent event) {
    eventHandler.handle(event);
  }
}
