package com.epam.reportportal.extension.slack.parser.attachment.filler.block;

import static java.util.Optional.ofNullable;

import com.epam.reportportal.extension.slack.model.domain.template.Block;
import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import com.epam.reportportal.extension.slack.parser.attachment.filler.text.TextContainerFiller;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;

public class BlockFieldFiller implements BlockFiller {

  private final TextContainerFiller fieldFiller;

  public BlockFieldFiller(TextContainerFiller fieldFiller) {
    this.fieldFiller = fieldFiller;
  }

  @Override
  public void fill(Block block, Map<TemplateProperty, String> fieldsMapping) {
    ofNullable(block.getFields()).filter(CollectionUtils::isNotEmpty).ifPresent(
        fields -> fields.forEach(f -> fieldFiller.fill(f, fieldsMapping)));
  }

}
