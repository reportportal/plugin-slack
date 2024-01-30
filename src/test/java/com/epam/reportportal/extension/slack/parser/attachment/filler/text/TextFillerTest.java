package com.epam.reportportal.extension.slack.parser.attachment.filler.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import com.epam.reportportal.extension.slack.model.domain.template.text.TextContainer;
import com.epam.reportportal.extension.slack.parser.wildcard.WildcardReplacer;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class TextFillerTest {

  private final WildcardReplacer wildcardReplacer = mock(WildcardReplacer.class);

  private final TextContainerFiller textContainerFiller = new TextContainerFiller(wildcardReplacer);


  @Test
  void shouldNotInvokeReplacerWhenTextIsNull() {
    TextContainer textContainer = mock(TextContainer.class);

    when(textContainer.getText()).thenReturn(null);

    textContainerFiller.fill(textContainer, new HashMap<>());

    verify(wildcardReplacer, times(0)).replace(anyString(), anyMap());

  }

  @Test
  void shouldNotInvokeReplacerWhenTextIsEmpty() {
    TextContainer textContainer = mock(TextContainer.class);

    when(textContainer.getText()).thenReturn("");

    textContainerFiller.fill(textContainer, new HashMap<>());

    verify(wildcardReplacer, times(0)).replace(anyString(), anyMap());
  }

  @Test
  void shouldNotInvokeReplacerWhenTextIsNotEmpty() {
    TextContainer textContainer = mock(TextContainer.class);
    final Map<TemplateProperty, String> fieldsMapping = new HashMap<>();

    final String replacedText = "Replaced text";

    when(textContainer.getText()).thenReturn("Some text");

    when(wildcardReplacer.replace(textContainer.getText(), fieldsMapping)).thenReturn(
        replacedText);

    textContainerFiller.fill(textContainer, fieldsMapping);

    verify(wildcardReplacer, times(1)).replace(textContainer.getText(), fieldsMapping);

    final ArgumentCaptor<String> textCaptor = ArgumentCaptor.forClass(String.class);

    verify(textContainer, times(1)).setText(textCaptor.capture());

    assertEquals(replacedText, textCaptor.getValue());
  }

}