package com.epam.reportportal.extension.slack.event.launch.analysis;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.epam.reportportal.extension.event.LaunchUniqueErrorAnalysisFinishEvent;
import com.epam.reportportal.extension.slack.event.handler.launch.LaunchEventHandler;
import org.junit.jupiter.api.Test;

class SlackUniqueErrorAnalysisFinishEventListenerTest {

  private final LaunchEventHandler<LaunchUniqueErrorAnalysisFinishEvent> eventHandler = mock(LaunchEventHandler.class);

  private final SlackUniqueErrorAnalysisFinishEventListener eventListener = new SlackUniqueErrorAnalysisFinishEventListener(eventHandler);

  @Test
  void shouldInvokeHandler() {
    final LaunchUniqueErrorAnalysisFinishEvent event = new LaunchUniqueErrorAnalysisFinishEvent(1L);
    eventListener.onApplicationEvent(event);

    verify(eventHandler, times(1)).handle(event);
  }

}