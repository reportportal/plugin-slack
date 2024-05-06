package com.epam.reportportal.extension.slack.info.impl;

import static java.util.Optional.ofNullable;

import com.epam.reportportal.extension.slack.info.PluginInfoProvider;
import com.epam.reportportal.rules.exception.ErrorType;
import com.epam.reportportal.rules.exception.ReportPortalException;
import com.epam.ta.reportportal.entity.enums.IntegrationGroupEnum;
import com.epam.ta.reportportal.entity.integration.IntegrationType;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PluginInfoProviderImpl implements PluginInfoProvider {

  private static final String BINARY_DATA_KEY = "binaryData";
  private static final String DESCRIPTION_KEY = "description";

  private static final String FIELDS_KEY = "ruleFields";
  private static final String METADATA_KEY = "metadata";

  private static final String PLUGIN_DESCRIPTION =
      "Reinforce your ReportPortal instance with Slack integration. Be informed about test result finish in real time in your Slack channel.";
  public static final Map<String, Object> PLUGIN_METADATA = new HashMap<>();

  static {
    PLUGIN_METADATA.put("isIntegrationsAllowed", false);
  }

  private final String resourcesDir;
  private final String propertyFile;

  public PluginInfoProviderImpl(String resourcesDir, String propertyFile) {
    this.resourcesDir = resourcesDir;
    this.propertyFile = propertyFile;
  }

  @Override
  public IntegrationType provide(IntegrationType integrationType) {
    integrationType.setIntegrationGroup(IntegrationGroupEnum.NOTIFICATION);
    loadBinaryDataInfo(integrationType);
    updateDescription(integrationType);
    updateMetadata(integrationType);
    addFieldsInfo(integrationType);
    return integrationType;
  }

  private void loadBinaryDataInfo(IntegrationType integrationType) {
    Map<String, Object> details = integrationType.getDetails().getDetails();
    if (ofNullable(details.get(BINARY_DATA_KEY)).isEmpty()) {
      try (InputStream propertiesStream = Files.newInputStream(
          Paths.get(resourcesDir, propertyFile))) {
        Properties binaryDataProperties = new Properties();
        binaryDataProperties.load(propertiesStream);
        Map<String, String> binaryDataInfo = binaryDataProperties.entrySet()
            .stream()
            .collect(HashMap::new,
                (map, entry) -> map.put(String.valueOf(entry.getKey()),
                    String.valueOf(entry.getValue())),
                HashMap::putAll
            );
        details.put(BINARY_DATA_KEY, binaryDataInfo);
      } catch (IOException ex) {
        throw new ReportPortalException(ErrorType.UNABLE_TO_LOAD_BINARY_DATA, ex.getMessage());
      }
    }
  }

  private void updateDescription(IntegrationType integrationType) {
    Map<String, Object> details = integrationType.getDetails().getDetails();
    details.put(DESCRIPTION_KEY, PLUGIN_DESCRIPTION);
  }

  private void updateMetadata(IntegrationType integrationType) {
    Map<String, Object> details = integrationType.getDetails().getDetails();
    details.put(METADATA_KEY, PLUGIN_METADATA);
  }

  private void addFieldsInfo(IntegrationType integrationType) {
    Map<String, Object> details = integrationType.getDetails().getDetails();
    Map<String, Object> ruleField = new HashMap<>();
    ruleField.put("name", "WebhookURL");
    ruleField.put("label", "WebhookURL");
    ruleField.put("type", "text");
    ruleField.put("default", "https//...");
		ruleField.put("required", true);
    Map<String, Object> validation = new HashMap<>();
    validation.put("type", "url");
    validation.put("errorMessage", "Field is Required. Please provide valid ...");
    ruleField.put("validation", validation);
    details.put(FIELDS_KEY, List.of(ruleField));
  }
}
