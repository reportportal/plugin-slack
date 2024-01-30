package com.epam.reportportal.extension.slack.parser.properties.launch;

import static com.epam.reportportal.extension.slack.model.enums.SlackIntegrationProperties.APP_URL;
import static com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty.LAUNCH_LINK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty;
import com.epam.ta.reportportal.dao.ProjectRepository;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.integration.IntegrationParams;
import com.epam.ta.reportportal.entity.launch.Launch;
import com.epam.ta.reportportal.entity.project.Project;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class LaunchLinkCollectorTest {

  private final ProjectRepository projectRepository = mock(ProjectRepository.class);

  private final LaunchLinkCollector launchLinkCollector = new LaunchLinkCollector(
      projectRepository);

  private final String BASE_URL = "http://localhost:8080";

  @Test
  void shouldReturnEmptyMapWhenNoAppUrlProperty() {
    final Launch launch = new Launch();
    final Integration integration = new Integration();

    final Map<DefaultTemplateProperty, String> map = launchLinkCollector.collect(launch, integration);

    assertTrue(map.isEmpty());
  }

  @Test
  void shouldReturnEmptyMapWhenProjectNotFound() {
    final Launch launch = new Launch();
    launch.setProjectId(1L);
    final Integration integration = new Integration();
    integration.setParams(new IntegrationParams(Map.of(APP_URL.getKey(), BASE_URL)));

    when(projectRepository.findById(launch.getProjectId())).thenReturn(Optional.empty());

    final Map<DefaultTemplateProperty, String> map = launchLinkCollector.collect(launch, integration);

    assertTrue(map.isEmpty());
  }

  @Test
  void shouldReturnMapWithLink() {
    final Launch launch = new Launch();
    launch.setId(111L);
    launch.setProjectId(1L);
    final Integration integration = new Integration();
    integration.setParams(new IntegrationParams(Map.of(APP_URL.getKey(), BASE_URL)));
    final Project project = new Project();
    project.setName("my_project");

    when(projectRepository.findById(launch.getProjectId())).thenReturn(Optional.of(project));

    final Map<DefaultTemplateProperty, String> map = launchLinkCollector.collect(launch, integration);

    assertFalse(map.isEmpty());
    assertEquals(BASE_URL + "/ui/#" + project.getName() + "/launches/all/" + launch.getId(),
        map.get(LAUNCH_LINK));
  }

}