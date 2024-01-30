package com.epam.reportportal.extension.slack.factory.color;

import static com.epam.reportportal.extension.slack.model.enums.SlackEventType.AUTO_ANALYSIS_FINISHED;
import static com.epam.reportportal.extension.slack.model.enums.SlackEventType.LAUNCH_FINISHED;
import static com.epam.reportportal.extension.slack.model.enums.SlackEventType.LAUNCH_STARTED;
import static com.epam.reportportal.extension.slack.model.enums.SlackEventType.UNIQUE_ERROR_ANALYSIS_FINISHED;
import static com.epam.reportportal.extension.slack.model.enums.SlackEventType.UNIQUE_ERROR_ANALYSIS_STARTED;

import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import com.epam.reportportal.extension.slack.parser.properties.color.ColorResolver;
import com.epam.reportportal.extension.slack.parser.properties.color.LaunchStatusColorResolver;
import com.epam.reportportal.extension.slack.parser.properties.color.StaticColorResolver;
import com.epam.ta.reportportal.entity.launch.Launch;
import java.util.LinkedHashMap;
import java.util.Map;

public class LaunchColorResolverFactory implements ColorResolverFactory<Launch> {

  public static final String LAUNCH_START_COLOR = AQUA_COLOR;
  public static final String LAUNCH_FAILED_COLOR = RED_COLOR;
  public static final String LAUNCH_PASSED_COLOR = GREEN_COLOR;

  public static final String AUTO_ANALYSIS_FINISH_COLOR = GREEN_COLOR;

  public static final String UNIQUE_ERROR_ANALYSIS_START_COLOR = AMBER_COLOR;
  public static final String UNIQUE_ERROR_ANALYSIS_FINISH_COLOR = GREEN_COLOR;

  private final Map<SlackEventType, ColorResolver<Launch>> mapping;

  public LaunchColorResolverFactory() {
    mapping = initMapping();
  }

  @Override
  public ColorResolver<Launch> get(SlackEventType eventType) {
    return mapping.get(eventType);
  }

  private Map<SlackEventType, ColorResolver<Launch>> initMapping() {
    Map<SlackEventType, ColorResolver<Launch>> mapping = new LinkedHashMap<>();
    mapping.put(LAUNCH_STARTED, new StaticColorResolver<>(LAUNCH_START_COLOR));
    mapping.put(LAUNCH_FINISHED,
        new LaunchStatusColorResolver(LAUNCH_PASSED_COLOR, LAUNCH_FAILED_COLOR));
    mapping.put(AUTO_ANALYSIS_FINISHED, new StaticColorResolver<>(AUTO_ANALYSIS_FINISH_COLOR));
    mapping.put(UNIQUE_ERROR_ANALYSIS_STARTED,
        new StaticColorResolver<>(UNIQUE_ERROR_ANALYSIS_START_COLOR));
    mapping.put(UNIQUE_ERROR_ANALYSIS_FINISHED,
        new StaticColorResolver<>(UNIQUE_ERROR_ANALYSIS_FINISH_COLOR));
    return mapping;
  }
}
