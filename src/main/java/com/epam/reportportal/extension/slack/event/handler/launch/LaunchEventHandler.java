package com.epam.reportportal.extension.slack.event.handler.launch;

import com.epam.reportportal.extension.event.LaunchEvent;
import com.epam.reportportal.extension.slack.event.handler.EventHandler;
import com.epam.reportportal.extension.slack.model.domain.SendAttachmentConfig;
import com.epam.reportportal.extension.slack.parser.attachment.provider.config.SendAttachmentConfigProvider;
import com.epam.reportportal.extension.slack.sender.AttachmentSender;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class LaunchEventHandler<T extends LaunchEvent<Long>> implements EventHandler<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(LaunchEventHandler.class);

  private final ThreadPoolTaskExecutor taskExecutor;

  private final SendAttachmentConfigProvider sendAttachmentConfigProvider;
  private final AttachmentSender attachmentSender;

  private final TransactionTemplate transactionTemplate;

  public LaunchEventHandler(
      ThreadPoolTaskExecutor taskExecutor,
      SendAttachmentConfigProvider sendAttachmentConfigProvider,
      AttachmentSender attachmentSender,
      PlatformTransactionManager transactionManager) {
    this.taskExecutor = taskExecutor;
    this.sendAttachmentConfigProvider = sendAttachmentConfigProvider;
    this.attachmentSender = attachmentSender;
    this.transactionTemplate = new TransactionTemplate(transactionManager);
  }


  @Override
  //TODO provideConfig should be inside submit() method, but in Service API Transaction is not committed so Launch status and other values are out-dated
  public void handle(T event) {
    try {
      provideConfig(event).ifPresent(
          config -> taskExecutor.submit(() -> attachmentSender.send(config)));
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  private Optional<SendAttachmentConfig> provideConfig(T event) {
    return transactionTemplate.execute(s -> sendAttachmentConfigProvider.provide(event));
  }

}
