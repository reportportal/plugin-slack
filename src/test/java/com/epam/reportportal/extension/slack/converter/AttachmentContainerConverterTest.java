package com.epam.reportportal.extension.slack.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.reportportal.extension.slack.client.payload.AttachmentSendParams;
import com.epam.reportportal.extension.slack.model.domain.SendAttachmentConfig;
import com.epam.reportportal.extension.slack.model.domain.template.Attachment;
import com.epam.reportportal.extension.slack.model.domain.template.AttachmentContainer;
import com.epam.reportportal.extension.slack.model.enums.SlackIntegrationProperties;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.integration.IntegrationParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AttachmentContainerConverterTest {

  private static final String TOKEN = "token";
  private static final String CHANNEL = "channel";

  private static final String CONVERTED_TEXT = "{converted}";

  private final ObjectMapper objectMapper = mock(ObjectMapper.class);

  private final AttachmentContainerConverter attachmentContainerConverter = new AttachmentContainerConverter(
      objectMapper);

  @Test
  void shouldReturnEmptyOptionalWhenNoToken() {
    final Optional<AttachmentSendParams> params = attachmentContainerConverter.convertToSendParams(
        getSendAttachmentConfig());

    assertFalse(params.isPresent());
  }

  @Test
  void shouldReturnEmptyOptionalWhenNoChannel() {

    final SendAttachmentConfig config = getSendAttachmentConfig();
    SlackIntegrationProperties.TOKEN.set(config.getIntegration(), TOKEN);

    final Optional<AttachmentSendParams> params = attachmentContainerConverter.convertToSendParams(
        config);

    assertFalse(params.isPresent());
  }

  @Test
  void shouldReturnEmptyOptionalWhenExceptionDuringJsonConvert() throws JsonProcessingException {

    final SendAttachmentConfig config = getSendAttachmentConfig();
    SlackIntegrationProperties.TOKEN.set(config.getIntegration(), TOKEN);
    SlackIntegrationProperties.CHANNEL.set(config.getIntegration(), CHANNEL);

    when(objectMapper.writeValueAsString(
        config.getAttachmentContainer().getAttachments())).thenThrow(
        JsonProcessingException.class);

    final Optional<AttachmentSendParams> params = attachmentContainerConverter.convertToSendParams(
        config);

    assertFalse(params.isPresent());
  }

  @Test
  void shouldConvert() throws JsonProcessingException {
    final SendAttachmentConfig config = getSendAttachmentConfig();
    SlackIntegrationProperties.TOKEN.set(config.getIntegration(), TOKEN);
    SlackIntegrationProperties.CHANNEL.set(config.getIntegration(), CHANNEL);

    when(objectMapper.writeValueAsString(
        config.getAttachmentContainer().getAttachments())).thenReturn(
        CONVERTED_TEXT);

    final Optional<AttachmentSendParams> params = attachmentContainerConverter.convertToSendParams(
        config);

    assertTrue(params.isPresent());

    final AttachmentSendParams convertedParams = params.get();

    assertEquals(TOKEN, convertedParams.getToken());
    assertEquals(CHANNEL, convertedParams.getChannel());
    assertEquals(CONVERTED_TEXT, convertedParams.getAttachmentText());
    assertEquals(config.getAttachmentContainer().getNotificationText(),
        convertedParams.getNotificationText());
  }


  private SendAttachmentConfig getSendAttachmentConfig() {
    final AttachmentContainer attachmentContainer = new AttachmentContainer();
    attachmentContainer.setNotificationText("Test notification text");
    attachmentContainer.setAttachments(List.of(new Attachment()));
    return SendAttachmentConfig.of(attachmentContainer, getIntegration());
  }

  private Integration getIntegration() {
    final Integration integration = new Integration();
    final IntegrationParams integrationParams = new IntegrationParams();
    integrationParams.setParams(new HashMap<>());
    integration.setParams(integrationParams);
    return integration;
  }

}