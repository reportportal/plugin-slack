package com.epam.reportportal.extension.slack.parser.attachment.filler;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.epam.reportportal.extension.slack.model.domain.template.Attachment;
import com.epam.reportportal.extension.slack.model.domain.template.Block;
import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import com.epam.reportportal.extension.slack.parser.attachment.filler.block.BlockFiller;
import com.epam.reportportal.extension.slack.parser.wildcard.WildcardReplacer;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;

class AttachmentFillerTest {

  private final WildcardReplacer wildcardReplacer = mock(WildcardReplacer.class);
  private final BlockFiller blockFiller = mock(BlockFiller.class);

  private final AttachmentFiller attachmentFiller = new AttachmentFiller(wildcardReplacer,
      blockFiller);

  @Test
  void shouldNotFailWhenNullFields() {
    attachmentFiller.fill(new Attachment(), new HashMap<>());

    verify(wildcardReplacer, times(0)).replace(anyString(),
        anyMapOf(TemplateProperty.class, String.class));
    verify(blockFiller, times(0)).fill(any(Block.class),
        anyMapOf(TemplateProperty.class, String.class));
  }

  @Test
  void shouldReplaceColor() {
    final Attachment attachment = new Attachment();
    attachment.setColor("color");
    attachmentFiller.fill(attachment, new HashMap<>());

    verify(wildcardReplacer, times(1)).replace(anyString(),
        anyMapOf(TemplateProperty.class, String.class));
  }

  @Test
  void shouldFillBlocks() {
    final Attachment attachment = new Attachment();
    attachment.setBlocks(List.of(new Block(), new Block()));
    attachmentFiller.fill(attachment, new HashMap<>());

    verify(blockFiller, times(2)).fill(any(Block.class),
        anyMapOf(TemplateProperty.class, String.class));
  }

}