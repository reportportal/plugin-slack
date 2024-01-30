package com.epam.reportportal.extension.slack.binary;

import static com.epam.reportportal.extension.slack.model.enums.SlackEventType.AUTO_ANALYSIS_FINISHED;
import static com.epam.reportportal.extension.slack.model.enums.SlackEventType.LAUNCH_FINISHED;
import static com.epam.reportportal.extension.slack.model.enums.SlackEventType.LAUNCH_STARTED;
import static com.epam.reportportal.extension.slack.model.enums.SlackEventType.UNIQUE_ERROR_ANALYSIS_FINISHED;
import static com.epam.reportportal.extension.slack.model.enums.SlackEventType.UNIQUE_ERROR_ANALYSIS_STARTED;

import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import java.io.File;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

public class MessageTemplateStore {

  private static final String MESSAGE_TEMPLATES_DIR = "message-templates";

  private final String resourcesDir;

  private final Map<SlackEventType, File> templateMapping;

  public MessageTemplateStore(String resourcesDir) {
    this.resourcesDir = resourcesDir;
    this.templateMapping = initTemplateMapping();
  }

  public Optional<File> get(SlackEventType eventType) {
    return Optional.ofNullable(templateMapping.get(eventType));
  }

  private Map<SlackEventType, File> initTemplateMapping() {
    return Map.of(LAUNCH_STARTED,
        getTemplateFile("start-launch.json"),
        LAUNCH_FINISHED,
        getTemplateFile("finish-launch.json"),
        AUTO_ANALYSIS_FINISHED,
        getTemplateFile("auto-analysis-finish.json"),
        UNIQUE_ERROR_ANALYSIS_STARTED,
        getTemplateFile("unique-error-analysis-start.json"),
        UNIQUE_ERROR_ANALYSIS_FINISHED,
        getTemplateFile("unique-error-analysis-finish.json"));
  }

  private File getTemplateFile(String s) {
    return Paths.get(resourcesDir, MESSAGE_TEMPLATES_DIR, s).toFile();
  }
}
