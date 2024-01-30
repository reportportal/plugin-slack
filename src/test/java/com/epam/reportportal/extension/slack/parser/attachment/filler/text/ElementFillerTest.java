package com.epam.reportportal.extension.slack.parser.attachment.filler.text;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import com.epam.reportportal.extension.slack.model.domain.template.text.Element;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ElementFillerTest {

  private final TextContainerFiller textContainerFiller = mock(TextContainerFiller.class);

  private final ElementFiller elementFiller = new ElementFiller(textContainerFiller);

  @Test
  void shouldInvokeTextFillerOnceForOuterElements() {
    final Element firstOuterElement = new Element();
    final Element secondOuterElement = new Element();

    final Map<TemplateProperty, String> fieldsMapping = new HashMap<>();

    elementFiller.fill(List.of(firstOuterElement, secondOuterElement), fieldsMapping);

    verify(textContainerFiller, times(2)).fill(any(Element.class), eq(fieldsMapping));

    verify(textContainerFiller, times(1)).fill(firstOuterElement, fieldsMapping);
    verify(textContainerFiller, times(1)).fill(secondOuterElement, fieldsMapping);
  }

  @Test
  void shouldInvokeTextFillerOnceForEachElement() {
    final Element firstOuter = new Element();
    firstOuter.setElements(List.of(new Element(), new Element()));

    final Element secondOuter = new Element();
    final Element secondOuterFirstInner = new Element();

    secondOuterFirstInner.setElements(List.of(new Element(), new Element(), new Element()));
    secondOuter.setElements(List.of(secondOuterFirstInner));

    final Map<TemplateProperty, String> fieldsMapping = new HashMap<>();

    elementFiller.fill(List.of(firstOuter, secondOuter), fieldsMapping);

    verify(textContainerFiller, times(8)).fill(any(Element.class), eq(fieldsMapping));

    verify(textContainerFiller, times(1)).fill(firstOuter, fieldsMapping);
    firstOuter.getElements()
        .forEach(e -> verify(textContainerFiller, times(1)).fill(e, fieldsMapping));

    verify(textContainerFiller, times(1)).fill(secondOuter, fieldsMapping);
    verify(textContainerFiller, times(1)).fill(secondOuterFirstInner, fieldsMapping);
    secondOuterFirstInner.getElements()
        .forEach(e -> verify(textContainerFiller, times(1)).fill(e, fieldsMapping));


  }

}