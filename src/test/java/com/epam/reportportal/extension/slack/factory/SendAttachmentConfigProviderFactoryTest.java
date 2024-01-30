package com.epam.reportportal.extension.slack.factory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import com.epam.reportportal.extension.slack.integration.IntegrationProvider;
import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import com.epam.ta.reportportal.dao.LaunchRepository;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class SendAttachmentConfigProviderFactoryTest {

  private final LaunchRepository launchRepository = mock(LaunchRepository.class);
  private final AttachmentContainerProviderFactory attachmentContainerProviderFactory = mock(
      AttachmentContainerProviderFactory.class);
  private final IntegrationProvider integrationProvider = mock(IntegrationProvider.class);

  private final SendAttachmentConfigProviderFactory factory = new SendAttachmentConfigProviderFactory(
      launchRepository, attachmentContainerProviderFactory, integrationProvider);

  @Test
  void shouldGetConfigProviderForEachEvent() {
    Arrays.stream(SlackEventType.values())
        .forEach(eventType -> assertNotNull(factory.get(eventType)));
  }
}