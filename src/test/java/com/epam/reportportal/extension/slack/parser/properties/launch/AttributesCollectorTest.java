package com.epam.reportportal.extension.slack.parser.properties.launch;

import static com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty.LAUNCH_ATTRIBUTES;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty;
import com.epam.ta.reportportal.entity.ItemAttribute;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.launch.Launch;
import java.util.LinkedHashSet;
import java.util.Map;
import org.junit.jupiter.api.Test;

class AttributesCollectorTest {

  private final AttributesCollector attributesCollector = new AttributesCollector();

  @Test
  void shouldCollectLaunchProperties() {
    final Launch launch = new Launch();
    final LinkedHashSet<ItemAttribute> attributes = new LinkedHashSet<>();
    attributes.add(new ItemAttribute("k2", "v2", false));
    attributes.add(new ItemAttribute(null, "v1", false));
    attributes.add(new ItemAttribute("k3", "v3", true));
    launch.setAttributes(attributes);

    final Map<DefaultTemplateProperty, String> launchProperties = attributesCollector.collect(
        launch,
        new Integration());

    assertEquals(1, launchProperties.size());
    assertEquals("k2:v2; :v1", launchProperties.get(LAUNCH_ATTRIBUTES));

  }

}