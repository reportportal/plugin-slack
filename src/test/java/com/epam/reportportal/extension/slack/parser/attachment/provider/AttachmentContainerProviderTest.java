package com.epam.reportportal.extension.slack.parser.attachment.provider;

import static com.epam.reportportal.extension.slack.model.enums.SlackEventType.LAUNCH_STARTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.reportportal.extension.slack.model.domain.template.AttachmentContainer;
import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import com.epam.reportportal.extension.slack.parser.attachment.filler.AttachmentContainerFiller;
import com.epam.reportportal.extension.slack.parser.attachment.provider.loader.AttachmentContainerLoader;
import com.epam.reportportal.extension.slack.parser.properties.aggregator.LaunchPropertiesAggregator;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.launch.Launch;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AttachmentContainerProviderTest {

  private final SlackEventType launchStartedEventType = LAUNCH_STARTED;

  private final AttachmentContainerLoader attachmentContainerLoader = mock(
      AttachmentContainerLoader.class);
  private final LaunchPropertiesAggregator launchPropertiesAggregator = mock(
      LaunchPropertiesAggregator.class);
  private final AttachmentContainerFiller attachmentContainerFiller = mock(
      AttachmentContainerFiller.class);

  private final AttachmentContainerProvider attachmentContainerProvider = new AttachmentContainerProvider(
      attachmentContainerLoader, launchPropertiesAggregator, attachmentContainerFiller);

  @Test
  void shouldProvideEmptyWhenContainerNotLoaded() {

    final Launch launch = new Launch();
    final Integration integration = new Integration();

    when(attachmentContainerLoader.load(any(SlackEventType.class))).thenReturn(Optional.empty());

    final Optional<AttachmentContainer> attachmentContainer = attachmentContainerProvider.provide(
        launch,
        integration, launchStartedEventType);

    assertFalse(attachmentContainer.isPresent());
  }

  @Test
  void shouldProvide() {
    final Launch launch = new Launch();
    final Integration integration = new Integration();

    final AttachmentContainer attachmentContainer = new AttachmentContainer();

    final Map<TemplateProperty, String> launchProperties = new HashMap<>();

    when(attachmentContainerLoader.load(any(SlackEventType.class))).thenReturn(
        Optional.of(attachmentContainer));
    when(launchPropertiesAggregator.aggregate(launch, integration)).thenReturn(launchProperties);

    final Optional<AttachmentContainer> providedContainer = attachmentContainerProvider.provide(
        launch,
        integration, launchStartedEventType);

    verify(attachmentContainerFiller, times(1)).fill(attachmentContainer, launchProperties);

    assertTrue(providedContainer.isPresent());
    assertEquals(launchStartedEventType.getMessage(),
        providedContainer.get().getNotificationText());
  }

}