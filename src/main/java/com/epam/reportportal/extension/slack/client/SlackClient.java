package com.epam.reportportal.extension.slack.client;

import com.epam.reportportal.extension.slack.client.payload.AttachmentSendParams;
import com.epam.ta.reportportal.exception.ReportPortalException;
import com.epam.ta.reportportal.ws.model.ErrorType;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import java.io.Closeable;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlackClient implements Closeable {

  private static final Logger LOGGER = LoggerFactory.getLogger(SlackClient.class);

  private final Slack slack;

  public SlackClient(Slack slack) {
    this.slack = slack;
  }

  public boolean sendAttachment(AttachmentSendParams sendAttachmentParams) {

    MethodsClient methods = slack.methods(sendAttachmentParams.getToken());

    ChatPostMessageRequest request = ChatPostMessageRequest.builder()
        .channel(sendAttachmentParams.getChannel())
        .text(sendAttachmentParams.getNotificationText())
        .attachmentsAsString(sendAttachmentParams.getAttachmentText())
        .build();

    try {
      final ChatPostMessageResponse chatPostMessageResponse = methods.chatPostMessage(request);
      return chatPostMessageResponse.isOk();
    } catch (IOException | SlackApiException e) {
      LOGGER.error(e.getMessage(), e);
      throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, e.getMessage());
    }

  }

  @Override
  public void close() {
    try {
      slack.close();
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
          "Unable to close Slack Client");
    }
  }
}
