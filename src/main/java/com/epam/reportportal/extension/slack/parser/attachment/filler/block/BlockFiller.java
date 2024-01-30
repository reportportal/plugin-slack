package com.epam.reportportal.extension.slack.parser.attachment.filler.block;

import com.epam.reportportal.extension.slack.model.domain.template.Block;
import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import java.util.Map;

public interface BlockFiller {

  void fill(Block block, Map<TemplateProperty, String> fieldsMapping);

}
