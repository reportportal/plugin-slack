package com.epam.reportportal.extension.slack.factory.color;

import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import com.epam.reportportal.extension.slack.parser.properties.color.ColorResolver;

public interface ColorResolverFactory<T> {

  String AMBER_COLOR = "#FFBF00";
  String AQUA_COLOR = "#00FFFF";
  String RED_COLOR = "#FF0000";
  String GREEN_COLOR = "#008000";

  ColorResolver<T> get(SlackEventType eventType);

}
