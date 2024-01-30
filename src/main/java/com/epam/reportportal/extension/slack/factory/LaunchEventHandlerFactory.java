package com.epam.reportportal.extension.slack.factory;

import com.epam.reportportal.extension.event.LaunchEvent;
import com.epam.reportportal.extension.slack.event.handler.launch.LaunchEventHandler;
import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

public class LaunchEventHandlerFactory {

  private final SendAttachmentConfigProviderFactory sendAttachmentConfigProviderFactory;

  private final AttachmentSenderFactory attachmentSenderFactory;

  private final ThreadPoolTaskExecutor taskExecutor;

  private final PlatformTransactionManager transactionManager;

  public LaunchEventHandlerFactory(
      SendAttachmentConfigProviderFactory sendAttachmentConfigProviderFactory,
      AttachmentSenderFactory attachmentSenderFactory,
      ThreadPoolTaskExecutor taskExecutor,
      PlatformTransactionManager transactionManager) {
    this.sendAttachmentConfigProviderFactory = sendAttachmentConfigProviderFactory;
    this.attachmentSenderFactory = attachmentSenderFactory;
    this.taskExecutor = taskExecutor;
    this.transactionManager = transactionManager;
  }

  public <T extends LaunchEvent<Long>> LaunchEventHandler<T> get(SlackEventType slackEventType) {
    return new LaunchEventHandler<>(
        taskExecutor,
        sendAttachmentConfigProviderFactory.get(slackEventType),
        attachmentSenderFactory.get(),
        transactionManager
    );
  }

}
