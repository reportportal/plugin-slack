package com.epam.reportportal.extension.slack.factory;

import com.epam.reportportal.extension.slack.binary.JsonObjectLoader;
import com.epam.reportportal.extension.slack.binary.MessageTemplateStore;
import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import com.epam.reportportal.extension.slack.parser.attachment.filler.AttachmentContainerFiller;
import com.epam.reportportal.extension.slack.parser.attachment.filler.AttachmentFiller;
import com.epam.reportportal.extension.slack.parser.attachment.filler.block.BlockElementFiller;
import com.epam.reportportal.extension.slack.parser.attachment.filler.block.BlockFieldFiller;
import com.epam.reportportal.extension.slack.parser.attachment.filler.block.DelegatingBlockFiller;
import com.epam.reportportal.extension.slack.parser.attachment.filler.text.ElementFiller;
import com.epam.reportportal.extension.slack.parser.attachment.filler.text.TextContainerFiller;
import com.epam.reportportal.extension.slack.parser.attachment.provider.AttachmentContainerProvider;
import com.epam.reportportal.extension.slack.parser.attachment.provider.loader.AttachmentContainerLoader;
import com.epam.reportportal.extension.slack.parser.wildcard.WildcardReplacer;
import com.epam.reportportal.extension.slack.utils.MemoizingSupplier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AttachmentContainerProviderFactory {

  private final LaunchPropertiesAggregatorFactory launchPropertiesAggregatorFactory;

  private final Supplier<AttachmentContainerLoader> attachmentContainerLoaderSupplier;

  private final Supplier<AttachmentContainerFiller> attachmentContainerFillerSupplier;

  private final Map<SlackEventType, AttachmentContainerProvider> mapping;

  public AttachmentContainerProviderFactory(
      LaunchPropertiesAggregatorFactory launchPropertiesAggregatorFactory,
      MessageTemplateStore messageTemplateStore,
      JsonObjectLoader jsonObjectLoader) {
    this.launchPropertiesAggregatorFactory = launchPropertiesAggregatorFactory;

    this.attachmentContainerLoaderSupplier = new MemoizingSupplier<>(
        () -> new AttachmentContainerLoader(
            messageTemplateStore, jsonObjectLoader));
    this.attachmentContainerFillerSupplier = new MemoizingSupplier<>(
        this::getAttachmentContainerFiller);

    this.mapping = initMapping();
  }

  public AttachmentContainerProvider get(SlackEventType eventType) {
    return mapping.get(eventType);
  }

  private AttachmentContainerFiller getAttachmentContainerFiller() {
    return new AttachmentContainerFiller(getAttachmentFiller());
  }

  private AttachmentFiller getAttachmentFiller() {
    final WildcardReplacer wildcardReplacer = new WildcardReplacer();
    return new AttachmentFiller(wildcardReplacer, getDelegatingBlockFiller(wildcardReplacer));
  }

  private DelegatingBlockFiller getDelegatingBlockFiller(WildcardReplacer wildcardReplacer) {

    final TextContainerFiller fieldFiller = new TextContainerFiller(wildcardReplacer);
    final BlockFieldFiller blockFieldFiller = new BlockFieldFiller(fieldFiller);

    final ElementFiller elementFiller = new ElementFiller(fieldFiller);
    final BlockElementFiller blockElementFiller = new BlockElementFiller(elementFiller);

    return new DelegatingBlockFiller(List.of(blockFieldFiller, blockElementFiller));
  }

  private Map<SlackEventType, AttachmentContainerProvider> initMapping() {
    return Arrays.stream(SlackEventType.values())
        .collect(Collectors.toMap(eventType -> eventType, this::getAttachmentContainerProvider));
  }


  private AttachmentContainerProvider getAttachmentContainerProvider(
      SlackEventType eventType) {
    return new AttachmentContainerProvider(attachmentContainerLoaderSupplier.get(),
        launchPropertiesAggregatorFactory.get(eventType),
        attachmentContainerFillerSupplier.get());
  }

}
