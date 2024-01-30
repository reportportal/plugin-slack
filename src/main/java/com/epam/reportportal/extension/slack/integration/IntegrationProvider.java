package com.epam.reportportal.extension.slack.integration;

import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import com.epam.ta.reportportal.dao.IntegrationRepository;
import com.epam.ta.reportportal.dao.IntegrationTypeRepository;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.integration.IntegrationType;
import java.util.Optional;
import org.apache.commons.lang3.BooleanUtils;

public class IntegrationProvider {

  private final IntegrationTypeRepository integrationTypeRepository;
  private final IntegrationRepository integrationRepository;

  private final String pluginId;

  public IntegrationProvider(
      IntegrationTypeRepository integrationTypeRepository,
      IntegrationRepository integrationRepository, String pluginId) {
    this.integrationTypeRepository = integrationTypeRepository;
    this.integrationRepository = integrationRepository;
    this.pluginId = pluginId;
  }

  //TODO CAN BE CHANGED TO SEND FOR EACH INTEGRATION
  public Optional<Integration> provide(Long projectId, SlackEventType eventType) {
    return integrationTypeRepository.findByName(pluginId)
        .filter(IntegrationType::isEnabled)
        .flatMap(
            integrationType -> integrationRepository.findAllByProjectIdAndTypeOrderByCreationDateDesc(
                projectId,
                integrationType
            ).stream().filter(Integration::isEnabled).filter(
                integration -> eventType.get(integration).map(BooleanUtils::toBoolean)
                    .orElse(Boolean.FALSE)).findFirst());

  }
}
