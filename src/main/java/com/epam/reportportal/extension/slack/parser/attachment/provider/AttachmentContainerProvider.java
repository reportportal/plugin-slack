package com.epam.reportportal.extension.slack.parser.attachment.provider;

import com.epam.reportportal.extension.slack.model.domain.template.AttachmentContainer;
import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import com.epam.reportportal.extension.slack.parser.attachment.filler.AttachmentContainerFiller;
import com.epam.reportportal.extension.slack.parser.attachment.provider.loader.AttachmentContainerLoader;
import com.epam.reportportal.extension.slack.parser.properties.aggregator.LaunchPropertiesAggregator;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.launch.Launch;
import java.util.Map;
import java.util.Optional;

public class AttachmentContainerProvider {

  private final AttachmentContainerLoader attachmentContainerLoader;
  private final LaunchPropertiesAggregator launchPropertiesAggregator;
  private final AttachmentContainerFiller attachmentContainerFiller;

  public AttachmentContainerProvider(
      AttachmentContainerLoader attachmentContainerLoader,
      LaunchPropertiesAggregator launchPropertiesAggregator,
      AttachmentContainerFiller attachmentContainerFiller) {
    this.attachmentContainerLoader = attachmentContainerLoader;
    this.launchPropertiesAggregator = launchPropertiesAggregator;
    this.attachmentContainerFiller = attachmentContainerFiller;
  }

  public Optional<AttachmentContainer> provide(Launch launch, Integration integration,
      SlackEventType eventType) {
    return attachmentContainerLoader.load(eventType)
        .map(attachmentContainer -> {
          final Map<TemplateProperty, String> launchProperties = getLaunchProperties(launch, integration);
          attachmentContainerFiller.fill(attachmentContainer, launchProperties);
          attachmentContainer.setNotificationText(eventType.getMessage());
          return attachmentContainer;
        });
  }

  private Map<TemplateProperty, String> getLaunchProperties(Launch launch, Integration integration) {
    return launchPropertiesAggregator.aggregate(launch, integration);
  }

}
