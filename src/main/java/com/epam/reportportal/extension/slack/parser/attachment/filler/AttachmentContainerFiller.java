package com.epam.reportportal.extension.slack.parser.attachment.filler;

import static java.util.Optional.ofNullable;

import com.epam.reportportal.extension.slack.model.domain.template.AttachmentContainer;
import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;

public class AttachmentContainerFiller {

  private final AttachmentFiller attachmentFiller;

  public AttachmentContainerFiller(AttachmentFiller attachmentFiller) {
    this.attachmentFiller = attachmentFiller;
  }

  public void fill(AttachmentContainer attachmentContainer, Map<TemplateProperty, String> fieldsMapping) {
    ofNullable(attachmentContainer.getAttachments()).filter(CollectionUtils::isNotEmpty)
        .ifPresent(
            attachments -> attachments.forEach(a -> attachmentFiller.fill(a, fieldsMapping)));
  }


}
