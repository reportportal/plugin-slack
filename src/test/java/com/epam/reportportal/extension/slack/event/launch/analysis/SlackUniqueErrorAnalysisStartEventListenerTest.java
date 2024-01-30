package com.epam.reportportal.extension.slack.event.launch.analysis;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.epam.reportportal.extension.event.LaunchStartUniqueErrorAnalysisEvent;
import com.epam.reportportal.extension.slack.event.handler.launch.LaunchEventHandler;
import org.junit.jupiter.api.Test;

class SlackUniqueErrorAnalysisStartEventListenerTest {

  private final LaunchEventHandler<LaunchStartUniqueErrorAnalysisEvent> eventHandler = mock(LaunchEventHandler.class);

  private final SlackUniqueErrorAnalysisStartEventListener eventListener = new SlackUniqueErrorAnalysisStartEventListener(eventHandler);

  @Test
  void shouldInvokeHandler() {
    final LaunchStartUniqueErrorAnalysisEvent event = new LaunchStartUniqueErrorAnalysisEvent(1L, 1L);
    eventListener.onApplicationEvent(event);

    verify(eventHandler, times(1)).handle(event);
  }

}