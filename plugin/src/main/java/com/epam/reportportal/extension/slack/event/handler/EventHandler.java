package com.epam.reportportal.extension.slack.event.handler;

/**
 * @author Andrei Piankouski
 */
public interface EventHandler<T> {

	void handle(T event);
}
