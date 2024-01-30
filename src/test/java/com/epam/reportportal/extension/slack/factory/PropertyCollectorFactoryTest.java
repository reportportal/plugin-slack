package com.epam.reportportal.extension.slack.factory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import com.epam.reportportal.extension.slack.factory.color.ColorResolverFactory;
import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import com.epam.ta.reportportal.dao.ProjectRepository;
import com.epam.ta.reportportal.entity.launch.Launch;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class PropertyCollectorFactoryTest {

  private final ColorResolverFactory<Launch> colorResolverFactory = mock(
      ColorResolverFactory.class);
  private final ProjectRepository projectRepository = mock(ProjectRepository.class);

  private final PropertyCollectorFactory factory = new PropertyCollectorFactory(
      colorResolverFactory, projectRepository);

  @Test
  void shouldGetPropertyCollectorForEachEvent() {
    Arrays.stream(SlackEventType.values())
        .forEach(eventType -> assertNotNull(factory.get(eventType)));
  }
}