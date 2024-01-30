package com.epam.reportportal.extension.slack.converter;

import static com.epam.reportportal.extension.slack.model.enums.SlackIntegrationProperties.CHANNEL;
import static com.epam.reportportal.extension.slack.model.enums.SlackIntegrationProperties.TOKEN;

import com.epam.reportportal.extension.slack.client.payload.AttachmentSendParams;
import com.epam.reportportal.extension.slack.model.domain.SendAttachmentConfig;
import com.epam.reportportal.extension.slack.model.domain.template.AttachmentContainer;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttachmentContainerConverter {

  private final Logger LOGGER = LoggerFactory.getLogger(AttachmentContainerConverter.class);

  private final ObjectMapper objectMapper;

  public AttachmentContainerConverter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public Optional<AttachmentSendParams> convertToSendParams(
      SendAttachmentConfig configuration) {
    final Integration integration = configuration.getIntegration();
    final AttachmentContainer attachmentContainer = configuration.getAttachmentContainer();
    return TOKEN.get(integration)
        .flatMap(token -> CHANNEL.get(integration)
            .flatMap(channel -> convertToText(attachmentContainer).map(
                attachmentText -> new AttachmentSendParams(token,
                    channel,
                    attachmentText,
                    attachmentContainer.getNotificationText()
                ))));
  }

  private Optional<String> convertToText(AttachmentContainer attachmentContainer) {
    try {
      return Optional.of(objectMapper.writeValueAsString(attachmentContainer.getAttachments()));
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage(), e);
      return Optional.empty();
    }
  }
}
