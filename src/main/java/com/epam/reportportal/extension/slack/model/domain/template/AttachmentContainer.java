package com.epam.reportportal.extension.slack.model.domain.template;

import java.util.List;


public class AttachmentContainer {

  private String notificationText;

  private List<Attachment> attachments;

  public String getNotificationText() {
    return notificationText;
  }

  public void setNotificationText(String notificationText) {
    this.notificationText = notificationText;
  }

  public List<Attachment> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<Attachment> attachments) {
    this.attachments = attachments;
  }
}
