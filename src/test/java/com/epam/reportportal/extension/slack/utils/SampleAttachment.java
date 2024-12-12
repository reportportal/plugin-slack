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

package com.epam.reportportal.extension.slack.utils;

public class SampleAttachment {

  public static final String SAMPLE_ATTACHMENT = """
      {
        "attachments": [
          {
            "color": "#FF0000",
            "blocks": [
              {
                "type": "section",
                "fields": [
                  {
                    "type": "mrkdwn",
                    "text": "*LAUNCH FINISHED:*\\n<http://localhost:8080/ui/#superadmin_personal/launches/all/56|Demo Api Tests#8>"
                  }
                ]
              },
              {
                "type": "section",
                "fields": [
                  {
                    "type": "mrkdwn",
                    "text": "*Start time:*\\n2024-12-11T21:14:52.790Z"
                  },
                  {
                    "type": "mrkdwn",
                    "text": "*Finish time:*\\n2024-12-11T21:14:53.778Z"
                  }
                ]
              },
              {
                "type": "section",
                "fields": [
                  {
                    "type": "mrkdwn",
                    "text": "*ID:*\\n56"
                  },
                  {
                    "type": "mrkdwn",
                    "text": "*UUID:*\\n7d45aa94-927e-430f-9819-e74253cb1e46"
                  }
                ]
              },
              {
                "type": "section",
                "fields": [
                  {
                    "type": "mrkdwn",
                    "text": "*Description*:\\nTest launch"
                  }
                ]
              },
              {
                "type": "section",
                "fields": [
                  {
                    "type": "mrkdwn",
                    "text": "*Attributes*:\\n[:string]"
                  }
                ]
              },
              {
                "type": "section",
                "text": {
                  "type": "mrkdwn",
                  "text": "\\n"
                }
              },
              {
                "type": "rich_text",
                "elements": [
                  {
                    "type": "rich_text_section",
                    "elements": [
                      {
                        "type": "text",
                        "text": "Execution Statistics:",
                        "style": {
                          "bold": true
                        }
                      },
                      {
                        "type": "text",
                        "text": "\\n"
                      }
                    ]
                  },
                  {
                    "type": "rich_text_quote",
                    "elements": [
                      {
                        "type": "emoji",
                        "name": "black_circle",
                        "unicode": "26ab"
                      },
                      {
                        "type": "text",
                        "text": " TOTAL:\\t2\\n"
                      },
                      {
                        "type": "emoji",
                        "name": "large_green_circle",
                        "unicode": "1f7e2"
                      },
                      {
                        "type": "text",
                        "text": " PASSED:\\t0\\n"
                      },
                      {
                        "type": "emoji",
                        "name": "red_circle",
                        "unicode": "1f534"
                      },
                      {
                        "type": "text",
                        "text": " FAILED:\\t2\\n"
                      },
                      {
                        "type": "emoji",
                        "name": "white_circle",
                        "unicode": "26aa"
                      },
                      {
                        "type": "text",
                        "text": " SKIPPED:\\t0"
                      }
                    ]
                  }
                ]
              },
              {
                "type": "section",
                "text": {
                  "type": "mrkdwn",
                  "text": "\\n"
                }
              },
              {
                "type": "rich_text",
                "elements": [
                  {
                    "type": "rich_text_section",
                    "elements": [
                      {
                        "type": "text",
                        "text": "Defect Statistics:",
                        "style": {
                          "bold": true
                        }
                      },
                      {
                        "type": "text",
                        "text": "\\n"
                      }
                    ]
                  },
                  {
                    "type": "rich_text_quote",
                    "elements": [
                      {
                        "type": "emoji",
                        "name": "large_red_square",
                        "unicode": "1f7e5"
                      },
                      {
                        "type": "text",
                        "text": " PRODUCT BUG GROUP:\\t\\t0\\n"
                      },
                      {
                        "type": "emoji",
                        "name": "large_yellow_square",
                        "unicode": "1f7e8"
                      },
                      {
                        "type": "text",
                        "text": " AUTOMATION BUG GROUP:\\t1\\n"
                      },
                      {
                        "type": "emoji",
                        "name": "large_blue_square",
                        "unicode": "1f7e6"
                      },
                      {
                        "type": "text",
                        "text": " SISTEM ISSUE GROUP: \\t\\t0\\n"
                      },
                      {
                        "type": "emoji",
                        "name": "white_square"
                      },
                      {
                        "type": "text",
                        "text": " NO DEFECT GROUP:\\t\\t\\t0\\n"
                      },
                      {
                        "type": "emoji",
                        "name": "black_square"
                      },
                      {
                        "type": "text",
                        "text": " TO INVESTIGATE GROUP:\\t\\t1"
                      }
                    ]
                  }
                ]
              }
            ]
          }
        ]
      }
      """;

}
