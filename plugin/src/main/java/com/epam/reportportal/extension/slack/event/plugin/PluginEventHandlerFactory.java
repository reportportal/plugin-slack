package com.epam.reportportal.extension.slack.event.plugin;

import com.epam.reportportal.extension.event.PluginEvent;
import com.epam.reportportal.extension.slack.event.EventHandlerFactory;
import com.epam.reportportal.extension.slack.event.handler.EventHandler;
import com.epam.reportportal.extension.slack.event.handler.plugin.PluginLoadedEventHandler;
import com.epam.reportportal.extension.slack.info.PluginInfoProvider;
import com.epam.ta.reportportal.dao.IntegrationRepository;
import com.epam.ta.reportportal.dao.IntegrationTypeRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrei Piankouski
 */
public class PluginEventHandlerFactory implements EventHandlerFactory<PluginEvent> {

  public static final String LOAD_KEY = "load";

  private final Map<String, EventHandler<PluginEvent>> eventHandlerMapping;

  public PluginEventHandlerFactory(IntegrationTypeRepository integrationTypeRepository,
       PluginInfoProvider pluginInfoProvider) {
    this.eventHandlerMapping = new HashMap<>();
    this.eventHandlerMapping.put(LOAD_KEY,
        new PluginLoadedEventHandler(integrationTypeRepository, pluginInfoProvider)
    );
  }

  @Override
  public EventHandler<PluginEvent> getEventHandler(String key) {
    return eventHandlerMapping.get(key);
  }
}
