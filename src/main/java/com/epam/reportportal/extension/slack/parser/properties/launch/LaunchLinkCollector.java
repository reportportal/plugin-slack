package com.epam.reportportal.extension.slack.parser.properties.launch;

import static com.epam.reportportal.extension.slack.model.enums.SlackIntegrationProperties.APP_URL;
import static com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty.LAUNCH_LINK;

import com.epam.reportportal.extension.slack.model.enums.template.DefaultTemplateProperty;
import com.epam.reportportal.extension.slack.parser.properties.PropertyCollector;
import com.epam.ta.reportportal.commons.validation.Suppliers;
import com.epam.ta.reportportal.dao.ProjectRepository;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.launch.Launch;
import com.epam.ta.reportportal.entity.project.Project;
import java.util.Collections;
import java.util.Map;

public class LaunchLinkCollector implements PropertyCollector<Launch, DefaultTemplateProperty> {

  public static final String UI_LAUNCH_PATH = "/ui/#{}/launches/all/{}";

  private final ProjectRepository projectRepository;

  public LaunchLinkCollector(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  @Override
  public Map<DefaultTemplateProperty, String> collect(Launch launch, Integration integration) {
    return APP_URL.get(integration)
        .flatMap(
            baseUrl -> projectRepository.findById(launch.getProjectId()).map(Project::getName)
                .map(projectName ->
                    baseUrl + Suppliers.formattedSupplier(UI_LAUNCH_PATH, projectName,
                        launch.getId()).get())
                .map(link -> Map.of(LAUNCH_LINK, link)))
        .orElseGet(Collections::emptyMap);
  }
}
