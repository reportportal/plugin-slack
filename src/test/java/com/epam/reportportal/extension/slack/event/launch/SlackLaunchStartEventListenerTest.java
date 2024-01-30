package com.epam.reportportal.extension.slack.event.launch;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.epam.reportportal.extension.event.LaunchFinishedPluginEvent;
import com.epam.reportportal.extension.slack.event.handler.launch.LaunchEventHandler;
import org.junit.jupiter.api.Test;

class SlackLaunchStartEventListenerTest {

  private final LaunchEventHandler<LaunchFinishedPluginEvent> eventHandler = mock(
      LaunchEventHandler.class);

  private final SlackLaunchFinishEventListener eventListener = new SlackLaunchFinishEventListener(
      eventHandler);

  @Test
  void shouldInvokeHandler() {
    final LaunchFinishedPluginEvent event = new LaunchFinishedPluginEvent(1L, 1L);
    eventListener.onApplicationEvent(event);

    verify(eventHandler, times(1)).handle(event);
  }

}