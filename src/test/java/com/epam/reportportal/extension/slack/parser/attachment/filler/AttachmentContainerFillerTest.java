package com.epam.reportportal.extension.slack.parser.attachment.filler;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.epam.reportportal.extension.slack.model.domain.template.Attachment;
import com.epam.reportportal.extension.slack.model.domain.template.AttachmentContainer;
import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;

class AttachmentContainerFillerTest {

  private final AttachmentFiller attachmentFiller = mock(AttachmentFiller.class);

  private final AttachmentContainerFiller containerFiller = new AttachmentContainerFiller(
      attachmentFiller);


  @Test
  void shouldNotFailWhenFieldsFieldIsNull() {
    containerFiller.fill(new AttachmentContainer(), new HashMap<>());

    verify(attachmentFiller, times(0)).fill(any(Attachment.class),
        anyMapOf(TemplateProperty.class, String.class));
  }

  @Test
  void shouldNotInvokeElementFillerWhenFieldsFieldIsEmpty() {
    final AttachmentContainer container = new AttachmentContainer();
    container.setAttachments(Collections.emptyList());

    containerFiller.fill(container, new HashMap<>());

    verify(attachmentFiller, times(0)).fill(any(Attachment.class),
        anyMapOf(TemplateProperty.class, String.class));
  }

  @Test
  void shouldInvokeElementFillerOnceWhenFieldsPresent() {
    final AttachmentContainer container = new AttachmentContainer();
    container.setAttachments(List.of(new Attachment(), new Attachment()));

    containerFiller.fill(container, new HashMap<>());

    verify(attachmentFiller, times(2)).fill(any(Attachment.class),
        anyMapOf(TemplateProperty.class, String.class));
  }

}