package com.epam.reportportal.extension.slack.sender;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.reportportal.extension.slack.client.SlackClient;
import com.epam.reportportal.extension.slack.client.payload.AttachmentSendParams;
import com.epam.reportportal.extension.slack.converter.AttachmentContainerConverter;
import com.epam.reportportal.extension.slack.model.domain.SendAttachmentConfig;
import com.epam.reportportal.extension.slack.model.domain.template.AttachmentContainer;
import com.epam.ta.reportportal.entity.integration.Integration;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AttachmentSenderTest {

  private final AttachmentContainerConverter attachmentContainerConverter = mock(
      AttachmentContainerConverter.class);
  private final SlackClient slackClient = mock(SlackClient.class);

  private final AttachmentSender attachmentSender = new AttachmentSender(
      attachmentContainerConverter, slackClient);

  @Test
  void shouldNotSendIfNotConverted() {
    when(attachmentContainerConverter.convertToSendParams(
        any(SendAttachmentConfig.class))).thenReturn(
        Optional.empty());

    attachmentSender.send(SendAttachmentConfig.of(new AttachmentContainer(), new Integration()));

    verify(slackClient, times(0)).sendAttachment(any(AttachmentSendParams.class));
  }

  @Test
  void shouldCatchExceptionDuringSending() {

    final SendAttachmentConfig sendAttachmentConfig = SendAttachmentConfig.of(
        new AttachmentContainer(),
        new Integration());

    when(attachmentContainerConverter.convertToSendParams(
        any(SendAttachmentConfig.class))).thenReturn(
        Optional.of(new AttachmentSendParams()));

    when(slackClient.sendAttachment(any(AttachmentSendParams.class))).thenThrow(
        new RuntimeException("Exception message"));

    attachmentSender.send(sendAttachmentConfig);


  }

  @Test
  void shouldSend() {
    final SendAttachmentConfig sendAttachmentConfig = SendAttachmentConfig.of(
        new AttachmentContainer(),
        new Integration());

    when(attachmentContainerConverter.convertToSendParams(
        any(SendAttachmentConfig.class))).thenReturn(
        Optional.of(new AttachmentSendParams()));

    attachmentSender.send(sendAttachmentConfig);

    verify(slackClient, times(1)).sendAttachment(any(AttachmentSendParams.class));
  }
}