package com.epam.reportportal.extension.slack.parser.attachment.provider.config;

import com.epam.reportportal.extension.event.LaunchEvent;
import com.epam.reportportal.extension.slack.integration.IntegrationProvider;
import com.epam.reportportal.extension.slack.model.domain.SendAttachmentConfig;
import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import com.epam.reportportal.extension.slack.parser.attachment.provider.AttachmentContainerProvider;
import com.epam.ta.reportportal.dao.LaunchRepository;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.launch.Launch;
import java.util.Optional;

public class SendAttachmentConfigProvider {

  private final SlackEventType eventType;

  private final AttachmentContainerProvider attachmentContainerProvider;
  private final IntegrationProvider integrationProvider;

  private final LaunchRepository launchRepository;

  public SendAttachmentConfigProvider(
      SlackEventType eventType,
      AttachmentContainerProvider attachmentContainerProvider,
      IntegrationProvider integrationProvider,
      LaunchRepository launchRepository) {
    this.eventType = eventType;
    this.attachmentContainerProvider = attachmentContainerProvider;
    this.integrationProvider = integrationProvider;
    this.launchRepository = launchRepository;
  }

  public <T extends LaunchEvent<Long>> Optional<SendAttachmentConfig> provide(T launchEvent) {
    return launchRepository.findById(launchEvent.getSource())
        .flatMap(launch -> getIntegration(launch)
            .flatMap(integration -> getSendConfiguration(launch, integration)));
  }

  private Optional<Integration> getIntegration(Launch launch) {
    return integrationProvider.provide(launch.getProjectId(), eventType);
  }

  private Optional<SendAttachmentConfig> getSendConfiguration(Launch launch,
      Integration integration) {
    return attachmentContainerProvider.provide(launch, integration, eventType).map(
        attachmentContainer -> SendAttachmentConfig.of(attachmentContainer, integration));
  }

}
