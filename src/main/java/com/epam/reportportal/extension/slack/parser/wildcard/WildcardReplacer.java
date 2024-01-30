package com.epam.reportportal.extension.slack.parser.wildcard;

import static java.util.Optional.ofNullable;

import com.epam.reportportal.extension.slack.model.domain.template.property.TemplateProperty;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class WildcardReplacer {

  //@formatter:off
  /**
   * 'LAUNCH STARTED:${LAUNCH_LINK!defaultLink}|${LAUNCH_NAME!defaultName}#${LAUNCH_NUMBER}'
   *
   *  In the message above next groups will be formed:
   *  - ${LAUNCH_LINK!defaultLink}
   *    - LAUNCH_LINK - 1st group
   *    - !defaultLink - 2nd group
   *      - defaultLink - 3rd group
   *  - ${LAUNCH_NAME!defaultName}
   *    - LAUNCH_NAME - 1st group
   *      - !defaultName - 2nd group
   *        - defaultName - 3rd group
   *  - ${LAUNCH_NUMBER}
   *    - LAUNCH_NUMBER - 1st group
   */
  //@formatter:on
  private static final Pattern DYNAMIC_VALUE_PATTERN = Pattern.compile(
      "\\$\\{([A-Za-z_0-9$]+)(!([^}]*))?}");

  private static final int VALUE_GROUP_INDEX = 1;
  private static final int DEFAULT_VALUE_GROUP_INDEX = 3;

  public String replace(String originalText, Map<TemplateProperty, String> propertiesMapping) {
    final Map<String, String> fields = getFields(propertiesMapping);
    Matcher matcher = DYNAMIC_VALUE_PATTERN.matcher(originalText);
    StringBuilder stringBuffer = new StringBuilder();
    while (matcher.find()) {
      String fieldKey = matcher.group(VALUE_GROUP_INDEX);
      String replaceWith = ofNullable(fields.get(fieldKey)).map(String::valueOf)
          .filter(StringUtils::isNotBlank)
          .orElseGet(() -> ofNullable(matcher.group(DEFAULT_VALUE_GROUP_INDEX)).orElse(""));
      matcher.appendReplacement(stringBuffer, replaceWith);
    }
    matcher.appendTail(stringBuffer);
    return stringBuffer.toString();
  }

  private Map<String, String> getFields(Map<TemplateProperty, String> propertiesMapping) {
    return propertiesMapping.entrySet().stream()
        .collect(LinkedHashMap::new,
            (m, v) -> m.put(v.getKey().getName(),
                v.getValue()),
            LinkedHashMap::putAll
        );
  }
}
