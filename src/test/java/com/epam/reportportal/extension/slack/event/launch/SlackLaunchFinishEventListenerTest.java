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

import static com.epam.reportportal.extension.slack.utils.SampleAttachment.SAMPLE_ATTACHMENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.reportportal.extension.event.LaunchFinishedPluginEvent;
import com.epam.reportportal.extension.slack.event.launch.resolver.AttachmentResolver;
import com.epam.reportportal.extension.slack.event.launch.resolver.SenderCaseMatcher;
import com.epam.reportportal.extension.slack.utils.MockData;
import com.epam.ta.reportportal.dao.LaunchRepository;
import com.epam.ta.reportportal.dao.ProjectRepository;
import com.epam.ta.reportportal.entity.launch.Launch;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

/**
 * @author <a href="mailto:siarhei_hrabko@epam.com">Siarhei Hrabko</a>
 */
@ExtendWith(MockitoExtension.class)
class SlackLaunchFinishEventListenerTest {

  private static final String LAUNCH_LINK = "http://localhost:8080/ui/#admin123/launches/all/55";

  @Mock
  RestTemplate restTemplate;// = new RestTemplate();
  @Mock
  ProjectRepository projectRepository;
  @Mock
  LaunchRepository launchRepository;
  @Mock(answer = Answers.CALLS_REAL_METHODS)
  SenderCaseMatcher senderCaseMatcher;
  @Mock
  AttachmentResolver attachmentResolver;

  @Test
  @Disabled("until RestTemplate initialization in SlackPluginExtension switched to @Autowired")
  void sendNotificationPositive() throws URISyntaxException {
    var slackLaunchFinishEventListener = new SlackLaunchFinishEventListener(projectRepository,
        launchRepository, senderCaseMatcher, attachmentResolver, restTemplate);

    when(projectRepository.findById(anyLong()))
        .thenReturn(Optional.of(MockData.getProjectSample()));
    when(launchRepository.findById(anyLong()))
        .thenReturn(Optional.of(MockData.getLaunch()));
    when(attachmentResolver.resolve(any(Launch.class), anyString()))
        .thenReturn(Optional.of(SAMPLE_ATTACHMENT));
    when(restTemplate.postForLocation(anyString(), anyString()))
        .thenReturn(new URI("http://localhost:8080"));

    slackLaunchFinishEventListener.onApplicationEvent(
        new LaunchFinishedPluginEvent(1L, 10L, LAUNCH_LINK));

    verify(restTemplate, times(1)).postForLocation(anyString(), anyString());

  }
}
