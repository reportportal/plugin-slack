package com.epam.reportportal.extension.slack.factory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import com.epam.reportportal.extension.slack.client.SlackClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class AttachmentSenderFactoryTest {

  private final ObjectMapper objectMapper = mock(ObjectMapper.class);
  private final SlackClient slackClient = mock(SlackClient.class);

  private final AttachmentSenderFactory factory = new AttachmentSenderFactory(objectMapper,
      slackClient);

  @Test
  void shouldGetSender() {
    assertNotNull(factory.get());
  }
}