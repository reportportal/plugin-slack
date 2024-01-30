package com.epam.reportportal.extension.slack.parser.attachment.provider.loader;

import com.epam.reportportal.extension.slack.binary.JsonObjectLoader;
import com.epam.reportportal.extension.slack.binary.MessageTemplateStore;
import com.epam.reportportal.extension.slack.model.domain.template.AttachmentContainer;
import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import java.util.Optional;

public class AttachmentContainerLoader {

  private final MessageTemplateStore messageTemplateStore;

  private final JsonObjectLoader jsonObjectLoader;

  public AttachmentContainerLoader(
      MessageTemplateStore messageTemplateStore,
      JsonObjectLoader jsonObjectLoader) {
    this.messageTemplateStore = messageTemplateStore;
    this.jsonObjectLoader = jsonObjectLoader;
  }

  public Optional<AttachmentContainer> load(SlackEventType eventType) {
    return messageTemplateStore.get(eventType)
        .flatMap(file -> jsonObjectLoader.load(file, AttachmentContainer.class));
  }

}
