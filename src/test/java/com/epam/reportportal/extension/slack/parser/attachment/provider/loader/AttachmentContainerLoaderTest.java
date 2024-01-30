package com.epam.reportportal.extension.slack.parser.attachment.provider.loader;

import static com.epam.reportportal.extension.slack.model.enums.SlackEventType.LAUNCH_STARTED;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.reportportal.extension.slack.binary.JsonObjectLoader;
import com.epam.reportportal.extension.slack.binary.MessageTemplateStore;
import com.epam.reportportal.extension.slack.model.domain.template.AttachmentContainer;
import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import java.io.File;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AttachmentContainerLoaderTest {

  private final MessageTemplateStore templateStore = mock(MessageTemplateStore.class);
  private final JsonObjectLoader jsonObjectLoader = mock(JsonObjectLoader.class);

  private final AttachmentContainerLoader attachmentContainerLoader = new AttachmentContainerLoader(
      templateStore, jsonObjectLoader);

  @Test
  void shouldLoadEmptyWhenFileNotFound() {
    when(templateStore.get(any(SlackEventType.class))).thenReturn(Optional.empty());

    final Optional<AttachmentContainer> attachmentContainer = attachmentContainerLoader.load(
        LAUNCH_STARTED);

    assertFalse(attachmentContainer.isPresent());
  }

  @Test
  void shouldLoadEmptyWhenContainerNotLoaded() {
    when(templateStore.get(any(SlackEventType.class))).thenReturn(Optional.of(new File("")));
    when(jsonObjectLoader.load(any(File.class), eq(AttachmentContainer.class))).thenReturn(
        Optional.empty());

    final Optional<AttachmentContainer> attachmentContainer = attachmentContainerLoader.load(
        LAUNCH_STARTED);

    assertFalse(attachmentContainer.isPresent());
  }

  @Test
  void shouldLoad() {
    when(templateStore.get(any(SlackEventType.class))).thenReturn(Optional.of(new File("")));
    when(jsonObjectLoader.load(any(File.class), eq(AttachmentContainer.class))).thenReturn(
        Optional.of(new AttachmentContainer()));

    final Optional<AttachmentContainer> attachmentContainer = attachmentContainerLoader.load(
        LAUNCH_STARTED);

    assertTrue(attachmentContainer.isPresent());
  }


}