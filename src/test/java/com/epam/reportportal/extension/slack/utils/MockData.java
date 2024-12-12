package com.epam.reportportal.extension.slack.utils;

import static com.epam.reportportal.extension.slack.event.launch.SlackLaunchFinishEventListener.PLUGIN_NOTIFICATION_TYPE;
import static com.epam.reportportal.extension.slack.event.launch.SlackLaunchFinishEventListener.SLACK_NOTIFICATION_ATTRIBUTE;
import static com.epam.reportportal.extension.slack.event.launch.SlackLaunchFinishEventListener.WEBHOOK_DETAILS;
import static com.epam.ta.reportportal.entity.enums.ProjectAttributeEnum.NOTIFICATIONS_ENABLED;

import com.epam.ta.reportportal.entity.attribute.Attribute;
import com.epam.ta.reportportal.entity.enums.SendCase;
import com.epam.ta.reportportal.entity.launch.Launch;
import com.epam.ta.reportportal.entity.project.Project;
import com.epam.ta.reportportal.entity.project.ProjectAttribute;
import com.epam.ta.reportportal.entity.project.email.SenderCase;
import com.epam.ta.reportportal.entity.project.email.SenderCaseOptions;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;

public class MockData {

  private static final String LAUNCH_NAME_1 = "Launch name 1";

  public static Project getProjectSample() {
    var project = new Project();

    project.setId(1L);
    project.setSenderCases(Collections.singleton(getSenderCase()));
    project.setProjectAttributes(getProjectAttributes());
    return project;
  }

  private static Set<ProjectAttribute> getProjectAttributes() {
    var attribute1 = new Attribute();
    attribute1.setId(13L);
    attribute1.setName(NOTIFICATIONS_ENABLED.getAttribute());

    var attribute2 = new Attribute();
    attribute2.setId(21L);
    attribute2.setName(SLACK_NOTIFICATION_ATTRIBUTE);

    ProjectAttribute projectAttribute1 = new ProjectAttribute()
        .withAttribute(attribute1)
        .withProject(new Project())
        .withValue("true");

    ProjectAttribute projectAttribute2 = new ProjectAttribute()
        .withAttribute(attribute2)
        .withProject(new Project())
        .withValue("true");

    return Set.of(projectAttribute1, projectAttribute2);

  }


  public static SenderCase getSenderCase() {
    var senderCase = new SenderCase();
    senderCase.setEnabled(true);
    senderCase.setType(PLUGIN_NOTIFICATION_TYPE);
    senderCase.setSendCase(SendCase.ALWAYS);
    senderCase.setLaunchNames(Set.of(LAUNCH_NAME_1));
    senderCase.setRuleDetails(getSenderCaseOptions());
    return senderCase;
  }

  public static Launch getLaunch() {
    var launch = new Launch();
    launch.setId(20L);
    launch.setName(LAUNCH_NAME_1);
    return launch;
  }

  public static SenderCaseOptions getSenderCaseOptions() {
    SenderCaseOptions senderCaseOptions = new SenderCaseOptions();
    senderCaseOptions.setOptions(
        Map.of(WEBHOOK_DETAILS,
            "https://hooks.slack.com/services/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"));
    return senderCaseOptions;
  }

  public static String readFileToString(String path) throws IOException {

    try (InputStream resourceAsStream = MockData.class.getClassLoader().getResourceAsStream(path)) {
      if (resourceAsStream != null) {
        return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
      } else {
        StringBuilder sb = new StringBuilder();
        try (Stream<String> lines = Files.lines(Paths.get(path))) {
          lines.forEach(sb::append);
        }
        return sb.toString();
      }
    }
  }
}

