package com.epam.reportportal.extension.slack.parser.properties.color;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.ta.reportportal.entity.launch.Launch;
import org.junit.jupiter.api.Test;

class StaticColorResolverTest {

  private final String color = "color";

  private final StaticColorResolver<Launch> staticColorResolver = new StaticColorResolver<>(color);

  @Test
  void shouldReturnStaticColor() {
    final String resolvedColor = staticColorResolver.resolve(new Launch());

    assertEquals(color, resolvedColor);
  }

}