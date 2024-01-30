package com.epam.reportportal.extension.slack.event.handler;

public interface EventHandler<T> {

  void handle(T event);
}
