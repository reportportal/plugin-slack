package com.epam.reportportal.extension.slack.parser.attachment.provider.config;

import static com.epam.reportportal.extension.slack.model.enums.SlackEventType.LAUNCH_STARTED;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.reportportal.extension.event.StartLaunchEvent;
import com.epam.reportportal.extension.slack.integration.IntegrationProvider;
import com.epam.reportportal.extension.slack.model.domain.SendAttachmentConfig;
import com.epam.reportportal.extension.slack.model.domain.template.AttachmentContainer;
import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import com.epam.reportportal.extension.slack.parser.attachment.provider.AttachmentContainerProvider;
import com.epam.ta.reportportal.dao.LaunchRepository;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.launch.Launch;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class SendAttachmentConfigProviderTest {

  private final SlackEventType launchStartedEventType = LAUNCH_STARTED;

  private final AttachmentContainerProvider attachmentContainerProvider = mock(
      AttachmentContainerProvider.class);

  private final IntegrationProvider integrationProvider = mock(IntegrationProvider.class);

  private final LaunchRepository launchRepository = mock(LaunchRepository.class);

  private final SendAttachmentConfigProvider configProvider = new SendAttachmentConfigProvider(
      launchStartedEventType, attachmentContainerProvider, integrationProvider, launchRepository);


  private final StartLaunchEvent startLaunchEvent = new StartLaunchEvent(1L);

  @Test
  void shouldNotProvideWhenLaunchNotFound() {
    when(launchRepository.findById(startLaunchEvent.getSource())).thenReturn(Optional.empty());

    final Optional<SendAttachmentConfig> config = configProvider.provide(startLaunchEvent);

    assertFalse(config.isPresent());
  }

  @Test
  void shouldNotProvideWhenIntegrationNotProvided() {
    when(launchRepository.findById(startLaunchEvent.getSource())).thenReturn(
        Optional.of(new Launch()));
    when(integrationProvider.provide(anyLong(), eq(launchStartedEventType))).thenReturn(
        Optional.empty());

    final Optional<SendAttachmentConfig> config = configProvider.provide(startLaunchEvent);

    assertFalse(config.isPresent());
  }

  @Test
  void shouldProvideEmptyWhenAttachmentContainerNotProvided() {
    when(launchRepository.findById(startLaunchEvent.getSource())).thenReturn(
        Optional.of(new Launch()));
    when(integrationProvider.provide(anyLong(), eq(launchStartedEventType))).thenReturn(
        Optional.of(new Integration()));
    when(attachmentContainerProvider.provide(any(Launch.class), any(Integration.class),
        eq(launchStartedEventType))).thenReturn(Optional.empty());

    final Optional<SendAttachmentConfig> config = configProvider.provide(startLaunchEvent);

    assertFalse(config.isPresent());
  }

  @Test
  void shouldProvideConfig() {
    when(launchRepository.findById(startLaunchEvent.getSource())).thenReturn(
        Optional.of(new Launch()));
    when(integrationProvider.provide(anyLong(), eq(launchStartedEventType))).thenReturn(
        Optional.of(new Integration()));
    when(attachmentContainerProvider.provide(any(Launch.class), any(Integration.class),
        eq(launchStartedEventType))).thenReturn(Optional.of(new AttachmentContainer()));

    final Optional<SendAttachmentConfig> config = configProvider.provide(startLaunchEvent);

    assertTrue(config.isPresent());

    assertNotNull(config.get().getAttachmentContainer());
    assertNotNull(config.get().getIntegration());
  }

}