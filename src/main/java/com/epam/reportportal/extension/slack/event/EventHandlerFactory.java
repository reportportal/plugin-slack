package com.epam.reportportal.extension.slack.event;

import com.epam.reportportal.extension.slack.event.handler.EventHandler;

public interface EventHandlerFactory<T> {

  EventHandler<T> getEventHandler(String key);
}
