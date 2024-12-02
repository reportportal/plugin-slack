package com.epam.reportportal.extension.slack.collector;

import com.epam.reportportal.extension.slack.model.template.TemplateProperty;
import java.util.Map;

public interface PropertyCollector<E, P extends TemplateProperty> {

  Map<P, String> collect(E entity);

}
