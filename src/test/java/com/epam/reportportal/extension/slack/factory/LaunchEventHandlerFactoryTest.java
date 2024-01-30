package com.epam.reportportal.extension.slack.factory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

class LaunchEventHandlerFactoryTest {

  private final SendAttachmentConfigProviderFactory attachmentConfigProviderFactory = mock(
      SendAttachmentConfigProviderFactory.class);
  private final AttachmentSenderFactory attachmentSenderFactory = mock(
      AttachmentSenderFactory.class);
  private final ThreadPoolTaskExecutor taskExecutor = mock(ThreadPoolTaskExecutor.class);
  private final PlatformTransactionManager transactionManager = mock(
      PlatformTransactionManager.class);


  private final LaunchEventHandlerFactory factory = new LaunchEventHandlerFactory(
      attachmentConfigProviderFactory, attachmentSenderFactory, taskExecutor, transactionManager);

  @Test
  void shouldGetEventHandlerForEachEvent() {
    Arrays.stream(SlackEventType.values())
        .forEach(eventType -> assertNotNull(factory.get(eventType)));
  }
}