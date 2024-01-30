package com.epam.reportportal.extension.slack.binary;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class MessageTemplateStoreTest {

  private final MessageTemplateStore store = new MessageTemplateStore("src/test/resources");

  @Test
  void shouldHaveFileForEachEventType() {
    Arrays.stream(SlackEventType.values()).forEach(eventType -> {
      final Optional<File> file = store.get(eventType);

      assertTrue(file.isPresent());
      assertTrue(file.get().exists());
    });
  }

}