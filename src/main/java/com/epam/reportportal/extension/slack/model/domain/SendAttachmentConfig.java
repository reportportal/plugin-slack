package com.epam.reportportal.extension.slack.model.domain;

import com.epam.reportportal.extension.slack.model.domain.template.AttachmentContainer;
import com.epam.ta.reportportal.entity.integration.Integration;

public final class SendAttachmentConfig {

  private final AttachmentContainer attachmentContainer;
  private final Integration integration;

  private SendAttachmentConfig(AttachmentContainer attachmentContainer,
      Integration integration) {
    this.attachmentContainer = attachmentContainer;
    this.integration = integration;
  }

  public AttachmentContainer getAttachmentContainer() {
    return attachmentContainer;
  }

  public Integration getIntegration() {
    return integration;
  }

  public static SendAttachmentConfig of(AttachmentContainer attachmentContainer,
      Integration integration) {
    return new SendAttachmentConfig(attachmentContainer, integration);
  }
}
