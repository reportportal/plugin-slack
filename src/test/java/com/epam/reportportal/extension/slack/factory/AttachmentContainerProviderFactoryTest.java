package com.epam.reportportal.extension.slack.factory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import com.epam.reportportal.extension.slack.binary.JsonObjectLoader;
import com.epam.reportportal.extension.slack.binary.MessageTemplateStore;
import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class AttachmentContainerProviderFactoryTest {

  private final LaunchPropertiesAggregatorFactory propertiesAggregatorFactory = mock(
      LaunchPropertiesAggregatorFactory.class);
  private final MessageTemplateStore templateStore = mock(MessageTemplateStore.class);
  private final JsonObjectLoader jsonObjectLoader = mock(JsonObjectLoader.class);

  private final AttachmentContainerProviderFactory factory = new AttachmentContainerProviderFactory(
      propertiesAggregatorFactory, templateStore, jsonObjectLoader);

  @Test
  void shouldGetContainerProviderForEachEvent() {
    Arrays.stream(SlackEventType.values())
        .forEach(eventType -> assertNotNull(factory.get(eventType)));
  }

}