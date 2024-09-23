package com.epam.reportportal.extension.slack.event.handler.plugin;

import com.epam.reportportal.extension.event.PluginEvent;
import com.epam.reportportal.extension.slack.event.handler.EventHandler;
import com.epam.reportportal.extension.slack.info.PluginInfoProvider;
import com.epam.ta.reportportal.dao.IntegrationRepository;
import com.epam.ta.reportportal.dao.IntegrationTypeRepository;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.integration.IntegrationParams;
import com.epam.ta.reportportal.entity.integration.IntegrationType;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

/**
 * @author Andrei Piankouski
 */
public class PluginLoadedEventHandler implements EventHandler<PluginEvent> {

  private final IntegrationTypeRepository integrationTypeRepository;
  private final PluginInfoProvider pluginInfoProvider;

  public PluginLoadedEventHandler(IntegrationTypeRepository integrationTypeRepository,
      PluginInfoProvider pluginInfoProvider) {
    this.integrationTypeRepository = integrationTypeRepository;
    this.pluginInfoProvider = pluginInfoProvider;
  }

  @Override
  public void handle(PluginEvent event) {
    integrationTypeRepository.findByName(event.getPluginId()).ifPresent(integrationType -> {
      integrationTypeRepository.save(pluginInfoProvider.provide(integrationType));
    });
  }
}
