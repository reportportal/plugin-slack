package com.epam.reportportal.extension.slack.parser.attachment.filler.block;

import static java.util.Optional.ofNullable;

import com.epam.reportportal.extension.slack.model.domain.template.Block;
import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import com.epam.reportportal.extension.slack.parser.attachment.filler.text.ElementFiller;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;

public class BlockElementFiller implements BlockFiller {

  private final ElementFiller elementFiller;

  public BlockElementFiller(ElementFiller elementFiller) {
    this.elementFiller = elementFiller;
  }

  @Override
  public void fill(Block block, Map<TemplateProperty, String> fieldsMapping) {
    ofNullable(block.getElements()).filter(CollectionUtils::isNotEmpty).ifPresent(
        elements -> elementFiller.fill(elements, fieldsMapping));
  }

}
