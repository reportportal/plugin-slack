package com.epam.reportportal.extension.slack.parser.attachment.filler.block;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.epam.reportportal.extension.slack.model.domain.template.Block;
import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import com.epam.reportportal.extension.slack.model.domain.template.text.Element;
import com.epam.reportportal.extension.slack.parser.attachment.filler.text.ElementFiller;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;

class BlockElementFillerTest {

  private final ElementFiller elementFiller = mock(ElementFiller.class);

  private final BlockElementFiller blockFiller = new BlockElementFiller(elementFiller);

  @Test
  void shouldNotFailWhenElementsFieldIsNull() {
    blockFiller.fill(new Block(), new HashMap<>());

    verify(elementFiller, times(0)).fill(anyListOf(Element.class),
        anyMapOf(TemplateProperty.class, String.class));
  }

  @Test
  void shouldNotInvokeElementFillerWhenElementsFieldIsEmpty() {
    final Block block = new Block();
    block.setElements(Collections.emptyList());

    blockFiller.fill(block, new HashMap<>());

    verify(elementFiller, times(0)).fill(anyListOf(Element.class),
        anyMapOf(TemplateProperty.class, String.class));
  }

  @Test
  void shouldInvokeElementFillerOnceWhenElementsPresent() {
    final Block block = new Block();
    block.setElements(List.of(new Element(), new Element()));

    blockFiller.fill(block, new HashMap<>());

    verify(elementFiller, times(1)).fill(anyListOf(Element.class),
        anyMapOf(TemplateProperty.class, String.class));
  }

}