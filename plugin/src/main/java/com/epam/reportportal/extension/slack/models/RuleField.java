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
package com.epam.reportportal.extension.slack.models;

/**
 * @author <a href="mailto:andrei_piankouski@epam.com">Andrei Piankouski</a>
 */
public class RuleField {

  private String name;

  private String label;

  private String description;

  private String type;

  private String placeholder;

  private Validation validation;

  public RuleField(String name, String label, String description, String type, String placeholder,
      Validation validation) {
    this.name = name;
    this.label = label;
    this.description = description;
    this.type = type;
    this.placeholder = placeholder;
    this.validation = validation;
  }

}
