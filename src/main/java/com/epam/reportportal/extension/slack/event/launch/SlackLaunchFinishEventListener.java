/*
 * Copyright 2024 EPAM Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.epam.reportportal.extension.slack.event.launch;

import com.epam.reportportal.extension.event.LaunchFinishedPluginEvent;
import com.epam.reportportal.extension.slack.event.launch.resolver.AttachmentResolver;
import com.epam.reportportal.extension.slack.event.launch.resolver.SenderCaseMatcher;
import com.epam.reportportal.rules.exception.ErrorType;
import com.epam.reportportal.rules.exception.ReportPortalException;
import com.epam.ta.reportportal.dao.LaunchRepository;
import com.epam.ta.reportportal.dao.ProjectRepository;
import com.epam.ta.reportportal.entity.enums.ProjectAttributeEnum;
import com.epam.ta.reportportal.entity.launch.Launch;
import com.epam.ta.reportportal.entity.project.Project;
import com.epam.ta.reportportal.entity.project.ProjectUtils;
import com.epam.ta.reportportal.entity.project.email.SenderCase;
import com.slack.api.Slack;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.context.ApplicationListener;

/**
 * @author <a href="mailto:andrei_piankouski@epam.com">Andrei Piankouski</a>
 */
public class SlackLaunchFinishEventListener implements
    ApplicationListener<LaunchFinishedPluginEvent> {

  private final static String SLACK_NOTIFICATION_ATTRIBUTE = "notifications.slack.enabled";

  private final static String WEBHOOK_DETAILS = "webhookURL";

  private final static String PLUGIN_NOTIFICATION_TYPE = "slack";

  private final ProjectRepository projectRepository;

  private final LaunchRepository launchRepository;

  private final SenderCaseMatcher senderCaseMatcher;

  private final AttachmentResolver attachmentResolver;


  public SlackLaunchFinishEventListener(
      ProjectRepository projectRepository, LaunchRepository launchRepository,
      SenderCaseMatcher senderCaseMatcher, AttachmentResolver attachmentResolver) {
    this.projectRepository = projectRepository;
    this.launchRepository = launchRepository;
    this.senderCaseMatcher = senderCaseMatcher;
    this.attachmentResolver = attachmentResolver;
  }

  @Override
  public void onApplicationEvent(LaunchFinishedPluginEvent event) {
    Project project = getProject(event.getProjectId());
    if (isNotificationsEnabled(project)) {
      Launch launch = getLaunch(event.getSource());
      processSenderCases(project, launch, event.getLaunchLink());
    }
  }

  private Project getProject(Long projectId) {
    return projectRepository.findById(projectId)
        .orElseThrow(() -> new ReportPortalException(ErrorType.PROJECT_NOT_FOUND, projectId));
  }

  private Launch getLaunch(Long launchId) {
    return launchRepository.findById(launchId)
        .orElseThrow(() -> new ReportPortalException(ErrorType.LAUNCH_NOT_FOUND, launchId));
  }

  private void processSenderCases(Project project, Launch launch, String launchLink) {
    project.getSenderCases().stream()
        .filter(SenderCase::isEnabled)
        .filter(this::isSlackSenderCase)
        .forEach(senderCase -> processSenderCase(senderCase, launch, launchLink));
  }

  private boolean isSlackSenderCase(SenderCase senderCase) {
    return senderCase.getType().equals(PLUGIN_NOTIFICATION_TYPE);
  }

  private void processSenderCase(SenderCase senderCase, Launch launch, String launchLink) {
    if (senderCaseMatcher.isSenderCaseMatched(senderCase, launch)) {
      sendNotification(senderCase, launch, launchLink);
    }
  }

  private void sendNotification(SenderCase senderCase, Launch launch, String launchLink) {
    Optional<String> webhookUrl = getWebhookUrl(senderCase);
    Optional<String> attachment = resolveAttachment(launch, launchLink);
    if (webhookUrl.isPresent() && attachment.isPresent()) {
      sendSlackNotification(webhookUrl.get(), attachment.get());
    }
  }

  private Optional<String> getWebhookUrl(SenderCase senderCase) {
    return Optional.ofNullable(
        (String) senderCase.getRuleDetails().getOptions().get(WEBHOOK_DETAILS));
  }

  private Optional<String> resolveAttachment(Launch launch, String launchLink) {
    return attachmentResolver.resolve(launch, launchLink);
  }

  private void sendSlackNotification(String webhookUrl, String attachment) {
    try (Slack slack = Slack.getInstance()) {
      slack.send(webhookUrl, attachment);
    } catch (Exception e) {
      throw new ReportPortalException("Failed to send Slack notification", e);
    }
  }

  private boolean isNotificationsEnabled(Project project) {
    Map<String, String> projectConfig = ProjectUtils.getConfigParameters(
        project.getProjectAttributes());
    return BooleanUtils.toBoolean(
        projectConfig.get(ProjectAttributeEnum.NOTIFICATIONS_ENABLED.getAttribute()))
        && BooleanUtils.toBoolean(projectConfig.get(SLACK_NOTIFICATION_ATTRIBUTE));
  }
}
