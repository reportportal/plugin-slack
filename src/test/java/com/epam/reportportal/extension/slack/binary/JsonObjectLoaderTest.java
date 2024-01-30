package com.epam.reportportal.extension.slack.binary;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.reportportal.extension.slack.model.domain.template.AttachmentContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class JsonObjectLoaderTest {

  private final ObjectMapper objectMapper = mock(ObjectMapper.class);

  private final JsonObjectLoader jsonObjectLoader = new JsonObjectLoader(objectMapper);

  @Test
  void shouldReturnEmptyWhenIOExceptionThrown() throws IOException {
    when(objectMapper.readValue(any(InputStream.class), eq(AttachmentContainer.class))).thenThrow(
        new IOException("Exception message"));

    final Optional<AttachmentContainer> container = jsonObjectLoader.load(
        new File("src/test/resources/message-templates/start-launch.json"),
        AttachmentContainer.class);

    assertFalse(container.isPresent());

  }

  @Test
  void shouldReturnEmptyWhenNullParsed() throws IOException {
    when(objectMapper.readValue(any(InputStream.class), eq(AttachmentContainer.class))).thenReturn(
        null);

    final Optional<AttachmentContainer> container = jsonObjectLoader.load(
        new File("src/test/resources/message-templates/start-launch.json"),
        AttachmentContainer.class);

    assertFalse(container.isPresent());

  }

  @Test
  void shouldReturnAttachmentContainer() throws IOException {
    when(objectMapper.readValue(any(InputStream.class), eq(AttachmentContainer.class))).thenReturn(
        new AttachmentContainer());

    final Optional<AttachmentContainer> container = jsonObjectLoader.load(
        new File("src/test/resources/message-templates/start-launch.json"),
        AttachmentContainer.class);

    assertTrue(container.isPresent());

  }

}