package com.epam.reportportal.extension.slack.parser.attachment.filler.text;

import static java.util.Optional.ofNullable;

import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import com.epam.reportportal.extension.slack.model.domain.template.text.Element;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;

public class ElementFiller {

  private final TextContainerFiller textFiller;

  public ElementFiller(TextContainerFiller textFiller) {
    this.textFiller = textFiller;
  }

  public void fill(List<Element> elements, Map<TemplateProperty, String> fieldsMapping) {
    elements.forEach(e -> {
      textFiller.fill(e, fieldsMapping);
      ofNullable(e.getElements()).filter(CollectionUtils::isNotEmpty)
          .ifPresent(innerElements -> fill(innerElements, fieldsMapping));
    });
  }

}
