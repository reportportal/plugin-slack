package com.epam.reportportal.extension.slack.parser.properties;

import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import com.epam.ta.reportportal.entity.integration.Integration;
import java.util.Map;

public interface PropertyCollector<E, P extends TemplateProperty> {

  Map<P, String> collect(E entity, Integration integration);

}
