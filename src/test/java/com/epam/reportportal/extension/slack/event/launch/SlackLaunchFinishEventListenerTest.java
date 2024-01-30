package com.epam.reportportal.extension.slack.event.launch;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.epam.reportportal.extension.event.StartLaunchEvent;
import com.epam.reportportal.extension.slack.event.handler.launch.LaunchEventHandler;
import org.junit.jupiter.api.Test;

class SlackLaunchFinishEventListenerTest {

  private final LaunchEventHandler<StartLaunchEvent> eventHandler = mock(LaunchEventHandler.class);

  private final SlackLaunchStartEventListener eventListener = new SlackLaunchStartEventListener(eventHandler);

  @Test
  void shouldInvokeHandler() {
    final StartLaunchEvent event = new StartLaunchEvent(1L);
    eventListener.onApplicationEvent(event);

    verify(eventHandler, times(1)).handle(event);
  }

}