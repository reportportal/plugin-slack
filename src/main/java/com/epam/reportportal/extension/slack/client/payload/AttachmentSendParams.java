package com.epam.reportportal.extension.slack.client.payload;

public class AttachmentSendParams {

  private String token;

  private String channel;

  private String attachmentText;
  private String notificationText;

  public AttachmentSendParams() {
  }

  public AttachmentSendParams(String token, String channel, String attachmentText,
      String notificationText) {
    this.token = token;
    this.channel = channel;
    this.attachmentText = attachmentText;
    this.notificationText = notificationText;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public String getAttachmentText() {
    return attachmentText;
  }

  public void setAttachmentText(String attachmentText) {
    this.attachmentText = attachmentText;
  }

  public String getNotificationText() {
    return notificationText;
  }

  public void setNotificationText(String notificationText) {
    this.notificationText = notificationText;
  }
}
