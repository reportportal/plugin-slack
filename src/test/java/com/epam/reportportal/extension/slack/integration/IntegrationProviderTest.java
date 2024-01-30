package com.epam.reportportal.extension.slack.integration;

import static com.epam.reportportal.extension.slack.model.enums.SlackEventType.LAUNCH_STARTED;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.ta.reportportal.dao.IntegrationRepository;
import com.epam.ta.reportportal.dao.IntegrationTypeRepository;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.integration.IntegrationParams;
import com.epam.ta.reportportal.entity.integration.IntegrationType;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class IntegrationProviderTest {

  private static final String PLUGIN_ID = "Slack";

  private static final Long PROJECT_ID = 1L;

  private final IntegrationTypeRepository integrationTypeRepository = mock(
      IntegrationTypeRepository.class);
  private final IntegrationRepository integrationRepository = mock(IntegrationRepository.class);

  private final IntegrationProvider integrationProvider = new IntegrationProvider(
      integrationTypeRepository, integrationRepository, PLUGIN_ID);

  @Test
  void shouldReturnEmptyWhenIntegrationTypeNotFound() {

    when(integrationTypeRepository.findByName(PLUGIN_ID)).thenReturn(Optional.empty());

    final Optional<Integration> integration = integrationProvider.provide(PROJECT_ID,
        LAUNCH_STARTED);

    assertFalse(integration.isPresent());
  }

  @Test
  void shouldReturnEmptyWhenIntegrationTypeNotEnabled() {
    final IntegrationType integrationType = new IntegrationType();
    integrationType.setEnabled(false);

    when(integrationTypeRepository.findByName(PLUGIN_ID)).thenReturn(
        Optional.of(integrationType));

    final Optional<Integration> integration = integrationProvider.provide(PROJECT_ID,
        LAUNCH_STARTED);

    assertFalse(integration.isPresent());
  }

  @Test
  void shouldReturnEmptyWhenIntegrationNotFound() {
    final IntegrationType integrationType = new IntegrationType();
    integrationType.setEnabled(true);

    when(integrationTypeRepository.findByName(PLUGIN_ID)).thenReturn(
        Optional.of(integrationType));

    when(integrationRepository.findAllByProjectIdAndTypeOrderByCreationDateDesc(PROJECT_ID,
        integrationType)).thenReturn(
        Collections.emptyList());

    final Optional<Integration> integration = integrationProvider.provide(PROJECT_ID,
        LAUNCH_STARTED);

    assertFalse(integration.isPresent());
  }

  @Test
  void shouldReturnEmptyWhenIntegrationNotEnabled() {
    final IntegrationType integrationType = new IntegrationType();
    integrationType.setEnabled(true);

    when(integrationTypeRepository.findByName(PLUGIN_ID)).thenReturn(
        Optional.of(integrationType));

    final Integration integration = new Integration();
    integration.setEnabled(false);

    when(integrationRepository.findAllByProjectIdAndTypeOrderByCreationDateDesc(PROJECT_ID,
        integrationType)).thenReturn(List.of(integration));

    final Optional<Integration> providedIntegration = integrationProvider.provide(PROJECT_ID,
        LAUNCH_STARTED);

    assertFalse(providedIntegration.isPresent());
  }

  @Test
  void shouldReturnEmptyWhenIntegrationEventNotExists() {
    final IntegrationType integrationType = new IntegrationType();
    integrationType.setEnabled(true);

    when(integrationTypeRepository.findByName(PLUGIN_ID)).thenReturn(
        Optional.of(integrationType));

    final Integration integration = new Integration();
    integration.setEnabled(true);

    when(integrationRepository.findAllByProjectIdAndTypeOrderByCreationDateDesc(PROJECT_ID,
        integrationType)).thenReturn(List.of(integration));

    final Optional<Integration> providedIntegration = integrationProvider.provide(PROJECT_ID,
        LAUNCH_STARTED);

    assertFalse(providedIntegration.isPresent());
  }

  @Test
  void shouldReturnEmptyWhenIntegrationEventNotEnabled() {
    final IntegrationType integrationType = new IntegrationType();
    integrationType.setEnabled(true);

    when(integrationTypeRepository.findByName(PLUGIN_ID)).thenReturn(
        Optional.of(integrationType));

    final Integration integration = new Integration();
    integration.setEnabled(true);
    integration.setParams(new IntegrationParams(Map.of(LAUNCH_STARTED.getValue(), "false")));

    when(integrationRepository.findAllByProjectIdAndTypeOrderByCreationDateDesc(PROJECT_ID,
        integrationType)).thenReturn(List.of(integration));

    final Optional<Integration> providedIntegration = integrationProvider.provide(PROJECT_ID,
        LAUNCH_STARTED);

    assertFalse(providedIntegration.isPresent());
  }

  @Test
  void shouldReturnFirstEnabled() {
    final IntegrationType integrationType = new IntegrationType();
    integrationType.setEnabled(true);

    when(integrationTypeRepository.findByName(PLUGIN_ID)).thenReturn(
        Optional.of(integrationType));

    final Integration disabledIntegration = new Integration();
    disabledIntegration.setEnabled(false);
    final Integration enabledIntegration = new Integration();
    enabledIntegration.setEnabled(true);
    enabledIntegration.setParams(new IntegrationParams(Map.of(LAUNCH_STARTED.getValue(), "true")));

    when(integrationRepository.findAllByProjectIdAndTypeOrderByCreationDateDesc(PROJECT_ID,
        integrationType)).thenReturn(List.of(disabledIntegration, enabledIntegration));

    final Optional<Integration> providedIntegration = integrationProvider.provide(PROJECT_ID,
        LAUNCH_STARTED);

    assertTrue(providedIntegration.isPresent());
  }

}