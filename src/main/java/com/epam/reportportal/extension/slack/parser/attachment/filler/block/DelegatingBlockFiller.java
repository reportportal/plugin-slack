package com.epam.reportportal.extension.slack.parser.attachment.filler.block;

import com.epam.reportportal.extension.slack.model.domain.template.Block;
import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import java.util.List;
import java.util.Map;

public class DelegatingBlockFiller implements BlockFiller {

  private final List<BlockFiller> delegates;

  public DelegatingBlockFiller(List<BlockFiller> delegates) {
    this.delegates = delegates;
  }

  @Override
  public void fill(Block block, Map<TemplateProperty, String> fieldsMapping) {
    delegates.forEach(d -> d.fill(block, fieldsMapping));
  }
}
