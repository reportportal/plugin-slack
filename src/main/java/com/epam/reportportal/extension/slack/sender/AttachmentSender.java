package com.epam.reportportal.extension.slack.sender;

import com.epam.reportportal.extension.slack.client.SlackClient;
import com.epam.reportportal.extension.slack.converter.AttachmentContainerConverter;
import com.epam.reportportal.extension.slack.model.domain.SendAttachmentConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttachmentSender {

  private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentSender.class);

  private final AttachmentContainerConverter attachmentContainerConverter;

  private final SlackClient slackClient;


  public AttachmentSender(AttachmentContainerConverter attachmentContainerConverter,
      SlackClient slackClient) {
    this.attachmentContainerConverter = attachmentContainerConverter;
    //    TODO ADD HTTP CLIENT CONFIG AND close() logic ON PLUGIN UNLOAD
    this.slackClient = slackClient;
  }

  public void send(SendAttachmentConfig configuration) {
    attachmentContainerConverter.convertToSendParams(configuration)
        .ifPresentOrElse(sendParams -> {
          try {
            slackClient.sendAttachment(sendParams);
          } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
          }
        }, () -> LOGGER.error("Unable to obtain send attachment params for integration: {}",
            configuration.getIntegration().getId()));
  }

}
