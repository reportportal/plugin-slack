package com.epam.reportportal.extension.slack.event.launch.analysis;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.epam.reportportal.extension.event.LaunchAutoAnalysisFinishEvent;
import com.epam.reportportal.extension.slack.event.handler.launch.LaunchEventHandler;
import org.junit.jupiter.api.Test;

class SlackAutoAnalysisFinishEventListenerTest {

  private final LaunchEventHandler<LaunchAutoAnalysisFinishEvent> eventHandler = mock(LaunchEventHandler.class);

  private final SlackAutoAnalysisFinishEventListener eventListener = new SlackAutoAnalysisFinishEventListener(eventHandler);

  @Test
  void shouldInvokeHandler() {
    final LaunchAutoAnalysisFinishEvent event = new LaunchAutoAnalysisFinishEvent(1L);
    eventListener.onApplicationEvent(event);

    verify(eventHandler, times(1)).handle(event);
  }

}