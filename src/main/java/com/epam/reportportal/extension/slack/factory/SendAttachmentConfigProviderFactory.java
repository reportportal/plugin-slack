package com.epam.reportportal.extension.slack.factory;

import com.epam.reportportal.extension.slack.integration.IntegrationProvider;
import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import com.epam.reportportal.extension.slack.parser.attachment.provider.config.SendAttachmentConfigProvider;
import com.epam.ta.reportportal.dao.LaunchRepository;

public class SendAttachmentConfigProviderFactory {

  private final LaunchRepository launchRepository;

  private final AttachmentContainerProviderFactory attachmentContainerProviderFactory;

  private final IntegrationProvider integrationProvider;

  public SendAttachmentConfigProviderFactory(LaunchRepository launchRepository,
      AttachmentContainerProviderFactory attachmentContainerProviderFactory,
      IntegrationProvider integrationProvider) {
    this.launchRepository = launchRepository;
    this.attachmentContainerProviderFactory = attachmentContainerProviderFactory;
    this.integrationProvider = integrationProvider;
  }

  public SendAttachmentConfigProvider get(SlackEventType slackEventType) {
    return new SendAttachmentConfigProvider(slackEventType,
        attachmentContainerProviderFactory.get(slackEventType),
        integrationProvider,
        launchRepository);
  }

}
