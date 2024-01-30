package com.epam.reportportal.extension.slack.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.reportportal.extension.slack.client.payload.AttachmentSendParams;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SlackClientTest {

  private final ChatPostMessageResponse response = mock(ChatPostMessageResponse.class);

  private final MethodsClient methodsClient = mock(MethodsClient.class);

  private final Slack slack = mock(Slack.class);

  private final SlackClient slackClient = new SlackClient(slack);

  @Test
  void shouldSendAndReturnTrue() throws SlackApiException, IOException {

    final AttachmentSendParams sendAttachmentParams = new AttachmentSendParams("token", "channel",
        "{}", "Test notification text");

    when(slack.methods(sendAttachmentParams.getToken())).thenReturn(methodsClient);
    when(methodsClient.chatPostMessage(any(ChatPostMessageRequest.class))).thenReturn(response);
    when(response.isOk()).thenReturn(Boolean.TRUE);

    final boolean sent = slackClient.sendAttachment(sendAttachmentParams);

    assertTrue(sent);

    final ArgumentCaptor<ChatPostMessageRequest> messageCaptor = ArgumentCaptor.forClass(
        ChatPostMessageRequest.class);
    verify(methodsClient, times(1)).chatPostMessage(messageCaptor.capture());

    final ChatPostMessageRequest messageRequest = messageCaptor.getValue();

    assertEquals(sendAttachmentParams.getChannel(), messageRequest.getChannel());
    assertEquals(sendAttachmentParams.getAttachmentText(), messageRequest.getAttachmentsAsString());
    assertEquals(sendAttachmentParams.getNotificationText(), messageRequest.getText());
  }

  @Test
  void shouldReturnFalseWhenNotSent() throws SlackApiException, IOException {

    final AttachmentSendParams sendAttachmentParams = new AttachmentSendParams("token", "channel",
        "{}", "Test notification text");

    when(slack.methods(sendAttachmentParams.getToken())).thenReturn(methodsClient);
    when(methodsClient.chatPostMessage(any(ChatPostMessageRequest.class))).thenReturn(response);
    when(response.isOk()).thenReturn(Boolean.FALSE);

    final boolean sent = slackClient.sendAttachment(sendAttachmentParams);

    assertFalse(sent);
  }

  @Test
  void shouldCloseInnerClient() throws Exception {
    slackClient.close();

    verify(slack, times(1)).close();
  }

}