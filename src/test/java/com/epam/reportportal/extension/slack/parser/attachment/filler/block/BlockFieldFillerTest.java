package com.epam.reportportal.extension.slack.parser.attachment.filler.block;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.epam.reportportal.extension.slack.model.domain.template.Block;
import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import com.epam.reportportal.extension.slack.model.domain.template.text.Field;
import com.epam.reportportal.extension.slack.parser.attachment.filler.text.TextContainerFiller;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;

class BlockFieldFillerTest {

  private final TextContainerFiller textFiller = mock(TextContainerFiller.class);

  private final BlockFieldFiller blockFiller = new BlockFieldFiller(textFiller);

  @Test
  void shouldNotFailWhenFieldsFieldIsNull() {
    blockFiller.fill(new Block(), new HashMap<>());

    verify(textFiller, times(0)).fill(any(Field.class),
        anyMapOf(TemplateProperty.class, String.class));
  }

  @Test
  void shouldNotInvokeElementFillerWhenFieldsFieldIsEmpty() {
    final Block block = new Block();
    block.setFields(Collections.emptyList());

    blockFiller.fill(block, new HashMap<>());

    verify(textFiller, times(0)).fill(any(Field.class),
        anyMapOf(TemplateProperty.class, String.class));
  }

  @Test
  void shouldInvokeElementFillerOnceWhenFieldsPresent() {
    final Block block = new Block();
    block.setFields(List.of(new Field(), new Field()));

    blockFiller.fill(block, new HashMap<>());

    verify(textFiller, times(2)).fill(any(Field.class),
        anyMapOf(TemplateProperty.class, String.class));
  }

}