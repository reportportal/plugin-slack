package com.epam.reportportal.extension.slack.parser.attachment.filler.block;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.epam.reportportal.extension.slack.model.domain.template.Block;
import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class DelegatingBlockFillerTest {

  private final BlockFiller firstFiller = mock(BlockFiller.class);
  private final BlockFiller secondFiller = mock(BlockFiller.class);

  private final DelegatingBlockFiller delegatingBlockFiller = new DelegatingBlockFiller(
      List.of(firstFiller, secondFiller));

  @Test
  void shouldInvokeEachDelegateOnce() {
    final Block block = new Block();
    final Map<TemplateProperty, String> fieldsMapping = new HashMap<>();

    delegatingBlockFiller.fill(block, fieldsMapping);

    verify(firstFiller, times(1)).fill(block, fieldsMapping);
    verify(secondFiller, times(1)).fill(block, fieldsMapping);
  }

}