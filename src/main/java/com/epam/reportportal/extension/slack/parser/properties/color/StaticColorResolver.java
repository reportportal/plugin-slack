package com.epam.reportportal.extension.slack.parser.properties.color;

public class StaticColorResolver<T> implements ColorResolver<T> {

  private final String color;

  public StaticColorResolver(String color) {
    this.color = color;
  }

  @Override
  public String resolve(T entity) {
    return color;
  }
}
