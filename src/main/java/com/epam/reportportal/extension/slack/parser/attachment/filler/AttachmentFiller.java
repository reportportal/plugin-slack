package com.epam.reportportal.extension.slack.parser.attachment.filler;

import static java.util.Optional.ofNullable;

import com.epam.reportportal.extension.slack.model.domain.template.Attachment;
import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import com.epam.reportportal.extension.slack.parser.attachment.filler.block.BlockFiller;
import com.epam.reportportal.extension.slack.parser.wildcard.WildcardReplacer;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class AttachmentFiller {

  private final WildcardReplacer wildcardReplacer;
  private final BlockFiller blockFiller;

  public AttachmentFiller(WildcardReplacer wildcardReplacer, BlockFiller blockFiller) {
    this.wildcardReplacer = wildcardReplacer;
    this.blockFiller = blockFiller;
  }

  public void fill(Attachment attachment, Map<TemplateProperty, String> fieldsMapping) {
    ofNullable(attachment.getColor()).filter(StringUtils::isNotEmpty)
        .ifPresent(c -> attachment.setColor(wildcardReplacer.replace(c, fieldsMapping)));
    ofNullable(attachment.getBlocks()).filter(CollectionUtils::isNotEmpty)
        .ifPresent(blocks -> blocks.forEach(b -> blockFiller.fill(b, fieldsMapping)));
  }

}
