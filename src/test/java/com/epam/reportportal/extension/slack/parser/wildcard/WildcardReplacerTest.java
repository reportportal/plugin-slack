package com.epam.reportportal.extension.slack.parser.wildcard;

import static com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty.LAUNCH_LINK;
import static com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty.LAUNCH_NUMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import com.epam.reportportal.extension.slack.model.domain.template.property.TextProperty;
import java.util.Map;
import org.junit.jupiter.api.Test;

class WildcardReplacerTest {

  private final WildcardReplacer wildcardReplacer = new WildcardReplacer();

  @Test
  void shouldReplaceProperties() {
    final String source = "LAUNCH STARTED:${LAUNCH_LINK!defaultLink}|${LAUNCH_NAME!defaultName}#${LAUNCH_NUMBER}";

    final Map<TemplateProperty, String> properties = Map.of(LAUNCH_LINK,
        "http://localhost:8080",
        LAUNCH_NUMBER, "2");

    final String replacedText = wildcardReplacer.replace(source, properties);

    assertEquals("LAUNCH STARTED:http://localhost:8080|defaultName#2", replacedText);
  }

  @Test
  void shouldReplaceStatisticsProperties() {
    final String source = "${statistics$executions$total!2}, ${statistics$defects$total!4}";

    final Map<TemplateProperty, String> properties = Map.of(
        new TextProperty("statistics$executions$total"), "10");

    final String replacedText = wildcardReplacer.replace(source, properties);

    assertEquals("10, 4", replacedText);
  }

}