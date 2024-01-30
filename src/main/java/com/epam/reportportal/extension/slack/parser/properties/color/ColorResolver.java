package com.epam.reportportal.extension.slack.parser.properties.color;

public interface ColorResolver<T> {

  String resolve(T entity);

}
