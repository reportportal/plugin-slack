package com.epam.reportportal.extension.slack.event.handler.launch;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.reportportal.extension.event.LaunchEvent;
import com.epam.reportportal.extension.event.StartLaunchEvent;
import com.epam.reportportal.extension.slack.model.domain.SendAttachmentConfig;
import com.epam.reportportal.extension.slack.model.domain.template.AttachmentContainer;
import com.epam.reportportal.extension.slack.parser.attachment.provider.config.SendAttachmentConfigProvider;
import com.epam.reportportal.extension.slack.sender.AttachmentSender;
import com.epam.ta.reportportal.entity.integration.Integration;
import java.util.Optional;
import java.util.concurrent.Future;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

class LaunchEventHandlerTest {

  private final static ThreadPoolTaskExecutor THROWING_TASK_EXECUTOR = new ThreadPoolTaskExecutor() {
    @Override
    public Future<?> submit(Runnable task) {
      throw new RuntimeException("Exception message");
    }
  };

  private final static ThreadPoolTaskExecutor TASK_EXECUTOR = new ThreadPoolTaskExecutor() {
    @Override
    public Future<?> submit(Runnable task) {
      task.run();
      return null;
    }
  };

  private final SendAttachmentConfigProvider sendAttachmentConfigProvider = mock(
      SendAttachmentConfigProvider.class);
  private final AttachmentSender attachmentSender = mock(AttachmentSender.class);
  private final PlatformTransactionManager transactionManager = mock(
      PlatformTransactionManager.class);

  private final LaunchEventHandler<LaunchEvent<Long>> launchStartHandler = new LaunchEventHandler<>(
      TASK_EXECUTOR, sendAttachmentConfigProvider, attachmentSender, transactionManager);

  @BeforeAll
  static void initialize() {
    THROWING_TASK_EXECUTOR.initialize();
    TASK_EXECUTOR.initialize();
  }

  @AfterAll
  static void shutdown() {
    THROWING_TASK_EXECUTOR.destroy();
    TASK_EXECUTOR.destroy();
  }

  @Test
  void shouldCatchException() {
    LaunchEventHandler<LaunchEvent<Long>> launchStartHandler = new LaunchEventHandler<>(
        THROWING_TASK_EXECUTOR, sendAttachmentConfigProvider, attachmentSender, transactionManager);

    launchStartHandler.handle(new StartLaunchEvent(1L));
  }

  @Test
  void shouldNotSendWhenConfigNotProvided() {
    final StartLaunchEvent event = new StartLaunchEvent(1L);
    when(sendAttachmentConfigProvider.provide(event)).thenReturn(Optional.empty());

    launchStartHandler.handle(event);

    verify(attachmentSender, times(0)).send(any(SendAttachmentConfig.class));
  }

  @Test
  void shouldSendWhenConfigProvided() {
    final StartLaunchEvent event = new StartLaunchEvent(1L);
    when(sendAttachmentConfigProvider.provide(event)).thenReturn(
        Optional.of(SendAttachmentConfig.of(new AttachmentContainer(), new Integration())));

    launchStartHandler.handle(event);

    verify(attachmentSender, times(1)).send(any(SendAttachmentConfig.class));
  }

}