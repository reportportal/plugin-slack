package com.epam.reportportal.extension.slack.factory;

import com.epam.reportportal.extension.slack.client.SlackClient;
import com.epam.reportportal.extension.slack.converter.AttachmentContainerConverter;
import com.epam.reportportal.extension.slack.sender.AttachmentSender;
import com.epam.reportportal.extension.slack.utils.MemoizingSupplier;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.Supplier;

public class AttachmentSenderFactory {

  private final Supplier<AttachmentContainerConverter> attachmentContainerConverterSupplier;
  private final Supplier<AttachmentSender> attachmentSenderSupplier;

  public AttachmentSenderFactory(ObjectMapper objectMapper, SlackClient slackClient) {
    this.attachmentContainerConverterSupplier = new MemoizingSupplier<>(
        () -> new AttachmentContainerConverter(objectMapper));

//    TODO ADD SLACK CLIENT CONNECTIONS CONFIGURATION
    this.attachmentSenderSupplier = new MemoizingSupplier<>(
        () -> new AttachmentSender(attachmentContainerConverterSupplier.get(), slackClient));
  }

  public AttachmentSender get() {
    return attachmentSenderSupplier.get();
  }
}
