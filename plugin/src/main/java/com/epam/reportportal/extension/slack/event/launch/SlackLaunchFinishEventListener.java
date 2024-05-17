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
import com.epam.reportportal.extension.slack.event.handler.launch.LaunchEventHandler;
import com.epam.ta.reportportal.dao.LaunchRepository;
import com.epam.ta.reportportal.dao.ProjectRepository;
import com.epam.ta.reportportal.entity.launch.Launch;
import com.epam.ta.reportportal.entity.project.Project;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import java.io.IOException;
import org.springframework.context.ApplicationListener;

/**
 * @author <a href="mailto:andrei_piankouski@epam.com">Andrei Piankouski</a>
 */
public class SlackLaunchFinishEventListener implements
    ApplicationListener<LaunchFinishedPluginEvent> {

  private final ProjectRepository projectRepository;

  private final LaunchRepository launchRepository;

  public SlackLaunchFinishEventListener(
      ProjectRepository projectRepository, LaunchRepository launchRepository) {
    this.projectRepository = projectRepository;
    this.launchRepository = launchRepository;
  }

  @Override
  public void onApplicationEvent(LaunchFinishedPluginEvent event) {
    Project project = projectRepository.findById(event.getProjectId())
        .orElseThrow(() -> new RuntimeException("Project not found"));
    Launch launch = launchRepository.findById(event.getSource())
        .orElseThrow(() -> new RuntimeException("Launch not found"));
    Slack slack = Slack.getInstance();
    String webhookUrl = System.getenv("https://hooks.slack.com/services/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");
    Payload payload = Payload.builder().text("Launch: " + launch.getId() + "in project: " + project.getId() + "was finished").build();
    try {
      WebhookResponse response = slack.send(webhookUrl, payload);
      System.out.println(response);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
