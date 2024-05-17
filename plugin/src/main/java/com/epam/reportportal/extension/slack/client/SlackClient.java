package com.epam.reportportal.extension.slack.client;

import com.epam.reportportal.rules.exception.ErrorType;
import com.epam.reportportal.rules.exception.ReportPortalException;
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

  public Slack getSlack() {
    return slack;
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
