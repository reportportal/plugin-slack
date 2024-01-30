package com.epam.reportportal.extension.slack.parser.attachment.filler.text;

import static java.util.Optional.ofNullable;

import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import com.epam.reportportal.extension.slack.model.domain.template.text.TextContainer;
import com.epam.reportportal.extension.slack.parser.wildcard.WildcardReplacer;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class TextContainerFiller {

  private final WildcardReplacer wildcardReplacer;

  public TextContainerFiller(WildcardReplacer wildcardReplacer) {
    this.wildcardReplacer = wildcardReplacer;
  }

  public void fill(TextContainer textContainer, Map<TemplateProperty, String> fieldsMapping) {
    ofNullable(textContainer.getText()).filter(StringUtils::isNotEmpty)
        .ifPresent(text -> textContainer.setText(wildcardReplacer.replace(text, fieldsMapping)));
  }

}
