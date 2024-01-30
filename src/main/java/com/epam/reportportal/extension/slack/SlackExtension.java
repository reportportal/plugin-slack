package com.epam.reportportal.extension.slack;

import com.epam.reportportal.extension.CommonPluginCommand;
import com.epam.reportportal.extension.PluginCommand;
import com.epam.reportportal.extension.ReportPortalExtensionPoint;
import com.epam.reportportal.extension.common.IntegrationTypeProperties;
import com.epam.reportportal.extension.event.LaunchAutoAnalysisFinishEvent;
import com.epam.reportportal.extension.event.LaunchFinishedPluginEvent;
import com.epam.reportportal.extension.event.LaunchStartUniqueErrorAnalysisEvent;
import com.epam.reportportal.extension.event.LaunchUniqueErrorAnalysisFinishEvent;
import com.epam.reportportal.extension.event.PluginEvent;
import com.epam.reportportal.extension.event.StartLaunchEvent;
import com.epam.reportportal.extension.slack.binary.JsonObjectLoader;
import com.epam.reportportal.extension.slack.binary.MessageTemplateStore;
import com.epam.reportportal.extension.slack.client.SlackClient;
import com.epam.reportportal.extension.slack.command.binary.GetFileCommand;
import com.epam.reportportal.extension.slack.command.connection.TestConnectionCommand;
import com.epam.reportportal.extension.slack.event.launch.SlackLaunchFinishEventListener;
import com.epam.reportportal.extension.slack.event.launch.SlackLaunchStartEventListener;
import com.epam.reportportal.extension.slack.event.launch.analysis.SlackAutoAnalysisFinishEventListener;
import com.epam.reportportal.extension.slack.event.launch.analysis.SlackUniqueErrorAnalysisFinishEventListener;
import com.epam.reportportal.extension.slack.event.launch.analysis.SlackUniqueErrorAnalysisStartEventListener;
import com.epam.reportportal.extension.slack.event.plugin.PluginEventHandlerFactory;
import com.epam.reportportal.extension.slack.event.plugin.SlackPluginEventListener;
import com.epam.reportportal.extension.slack.factory.AttachmentContainerProviderFactory;
import com.epam.reportportal.extension.slack.factory.AttachmentSenderFactory;
import com.epam.reportportal.extension.slack.factory.LaunchEventHandlerFactory;
import com.epam.reportportal.extension.slack.factory.LaunchPropertiesAggregatorFactory;
import com.epam.reportportal.extension.slack.factory.PropertyCollectorFactory;
import com.epam.reportportal.extension.slack.factory.SendAttachmentConfigProviderFactory;
import com.epam.reportportal.extension.slack.factory.color.ColorResolverFactory;
import com.epam.reportportal.extension.slack.factory.color.LaunchColorResolverFactory;
import com.epam.reportportal.extension.slack.info.impl.PluginInfoProviderImpl;
import com.epam.reportportal.extension.slack.integration.IntegrationProvider;
import com.epam.reportportal.extension.slack.model.enums.SlackEventType;
import com.epam.reportportal.extension.slack.utils.MemoizingSupplier;
import com.epam.ta.reportportal.dao.IntegrationRepository;
import com.epam.ta.reportportal.dao.IntegrationTypeRepository;
import com.epam.ta.reportportal.dao.LaunchRepository;
import com.epam.ta.reportportal.dao.ProjectRepository;
import com.epam.ta.reportportal.entity.launch.Launch;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.slack.api.Slack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.PostConstruct;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Extension
public class SlackExtension implements ReportPortalExtensionPoint, DisposableBean {

  private static final Logger LOGGER = LoggerFactory.getLogger(SlackExtension.class);

  public static final String BINARY_DATA_PROPERTIES_FILE_ID = "slack-binary-data.properties";

  private static final String PLUGIN_ID = "Slack";

  private final String resourcesDir;

  private final Supplier<Map<String, PluginCommand<?>>> pluginCommandMapping = new MemoizingSupplier<>(
      this::getCommands);
  private final Supplier<Map<String, CommonPluginCommand<?>>> commonCommandsMapping = new MemoizingSupplier<>(
      this::getCommonCommandsMapping);

  private final ObjectMapper objectMapper;
  private final JsonObjectLoader jsonObjectLoader;

  private final Supplier<ThreadPoolTaskExecutor> sendMessageExecutorSupplier;

  private final Supplier<SlackClient> slackClientSupplier;

  private final Supplier<IntegrationProvider> integrationProviderSupplier;

  private final Supplier<MessageTemplateStore> messageTemplateStoreSupplier;

  private final Supplier<LaunchEventHandlerFactory> launchEventHandlerFactorySupplier;

  private final Supplier<ApplicationListener<PluginEvent>> pluginLoadedListenerSupplier;

  private final Supplier<ApplicationListener<StartLaunchEvent>> launchStartEventListenerSupplier;
  private final Supplier<ApplicationListener<LaunchFinishedPluginEvent>> launchFinishEventListenerSupplier;

  private final Supplier<ApplicationListener<LaunchAutoAnalysisFinishEvent>> autoAnalysisFinishEventSupplier;

  private final Supplier<ApplicationListener<LaunchStartUniqueErrorAnalysisEvent>> uniqueErrorAnalysisStartEventSupplier;
  private final Supplier<ApplicationListener<LaunchUniqueErrorAnalysisFinishEvent>> uniqueErrorAnalysisFinishEventSupplier;

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private PlatformTransactionManager transactionManager;

  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private IntegrationTypeRepository integrationTypeRepository;

  @Autowired
  private IntegrationRepository integrationRepository;

  @Autowired
  private LaunchRepository launchRepository;

  public SlackExtension(Map<String, Object> initParams) {
    resourcesDir = IntegrationTypeProperties.RESOURCES_DIRECTORY.getValue(initParams)
        .map(String::valueOf).orElse("");
    objectMapper = configureObjectMapper();
    jsonObjectLoader = new JsonObjectLoader(objectMapper);

    sendMessageExecutorSupplier = new MemoizingSupplier<>(this::configureSendMessageExecutor);

    //    TODO ADD SLACK CLIENT CONNECTIONS CONFIGURATION
    slackClientSupplier = new MemoizingSupplier<>(() -> new SlackClient(Slack.getInstance()));

    integrationProviderSupplier = new MemoizingSupplier<>(
        () -> new IntegrationProvider(integrationTypeRepository, integrationRepository, PLUGIN_ID));

    messageTemplateStoreSupplier = new MemoizingSupplier<>(
        () -> new MessageTemplateStore(resourcesDir));

    launchEventHandlerFactorySupplier = new MemoizingSupplier<>(
        this::getLaunchEventHandlerFactory);

    pluginLoadedListenerSupplier = new MemoizingSupplier<>(
        () -> new SlackPluginEventListener(PLUGIN_ID,
            new PluginEventHandlerFactory(integrationTypeRepository,
                integrationRepository,
                new PluginInfoProviderImpl(resourcesDir, BINARY_DATA_PROPERTIES_FILE_ID)
            )
        ));

    launchStartEventListenerSupplier = new MemoizingSupplier<>(
        () -> new SlackLaunchStartEventListener(
            launchEventHandlerFactorySupplier.get().get(SlackEventType.LAUNCH_STARTED)));

    launchFinishEventListenerSupplier = new MemoizingSupplier<>(
        () -> new SlackLaunchFinishEventListener(
            launchEventHandlerFactorySupplier.get().get(SlackEventType.LAUNCH_FINISHED)));

    autoAnalysisFinishEventSupplier = new MemoizingSupplier<>(
        () -> new SlackAutoAnalysisFinishEventListener(
            launchEventHandlerFactorySupplier.get().get(SlackEventType.AUTO_ANALYSIS_FINISHED)));

    uniqueErrorAnalysisStartEventSupplier = new MemoizingSupplier<>(
        () -> new SlackUniqueErrorAnalysisStartEventListener(
            launchEventHandlerFactorySupplier.get()
                .get(SlackEventType.UNIQUE_ERROR_ANALYSIS_STARTED)));

    uniqueErrorAnalysisFinishEventSupplier = new MemoizingSupplier<>(
        () -> new SlackUniqueErrorAnalysisFinishEventListener(
            launchEventHandlerFactorySupplier.get()
                .get(SlackEventType.UNIQUE_ERROR_ANALYSIS_FINISHED)));
  }

  private LaunchEventHandlerFactory getLaunchEventHandlerFactory() {
    return new LaunchEventHandlerFactory(getSendAttachmentConfigProviderFactory(),
        getAttachmentSenderFactory(), sendMessageExecutorSupplier.get(), transactionManager);
  }

  private SendAttachmentConfigProviderFactory getSendAttachmentConfigProviderFactory() {
    return new SendAttachmentConfigProviderFactory(launchRepository,
        getAttachmentContainerProviderFactory(), integrationProviderSupplier.get());
  }

  private AttachmentContainerProviderFactory getAttachmentContainerProviderFactory() {
    return new AttachmentContainerProviderFactory(getLaunchPropertiesAggregatorFactory(),
        messageTemplateStoreSupplier.get(), jsonObjectLoader);
  }

  private LaunchPropertiesAggregatorFactory getLaunchPropertiesAggregatorFactory() {
    return new LaunchPropertiesAggregatorFactory(getPropertyCollectorFactory());
  }

  private PropertyCollectorFactory getPropertyCollectorFactory() {
    return new PropertyCollectorFactory(getLaunchColorResolverFactory(), projectRepository);
  }

  private ColorResolverFactory<Launch> getLaunchColorResolverFactory() {
    return new LaunchColorResolverFactory();
  }

  private AttachmentSenderFactory getAttachmentSenderFactory() {
    return new AttachmentSenderFactory(objectMapper, slackClientSupplier.get());
  }

  protected ObjectMapper configureObjectMapper() {
    ObjectMapper om = new ObjectMapper();
    om.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
    om.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    om.registerModule(new JavaTimeModule());
    return om;
  }

  protected ThreadPoolTaskExecutor configureSendMessageExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(10000);
    executor.setAllowCoreThreadTimeOut(true);
    executor.setThreadNamePrefix("slack-send-message");
    executor.setRejectedExecutionHandler((r, e) -> LOGGER.error("Send message task rejected"));
    return executor;
  }

  @PostConstruct
  public void createIntegration() {
    sendMessageExecutorSupplier.get().initialize();
    initListeners();
  }

  private void initListeners() {
    ApplicationEventMulticaster applicationEventMulticaster = applicationContext.getBean(
        AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME,
        ApplicationEventMulticaster.class
    );
    applicationEventMulticaster.addApplicationListener(pluginLoadedListenerSupplier.get());

    applicationEventMulticaster.addApplicationListener(launchStartEventListenerSupplier.get());
    applicationEventMulticaster.addApplicationListener(launchFinishEventListenerSupplier.get());

    applicationEventMulticaster.addApplicationListener(autoAnalysisFinishEventSupplier.get());

    applicationEventMulticaster.addApplicationListener(uniqueErrorAnalysisStartEventSupplier.get());
    applicationEventMulticaster.addApplicationListener(uniqueErrorAnalysisFinishEventSupplier.get());
  }

  @Override
  public void destroy() {
    removeListeners();
    sendMessageExecutorSupplier.get().destroy();
    slackClientSupplier.get().close();
  }

  private void removeListeners() {
    ApplicationEventMulticaster applicationEventMulticaster = applicationContext.getBean(
        AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME,
        ApplicationEventMulticaster.class
    );
    applicationEventMulticaster.removeApplicationListener(pluginLoadedListenerSupplier.get());

    applicationEventMulticaster.removeApplicationListener(launchStartEventListenerSupplier.get());
    applicationEventMulticaster.removeApplicationListener(launchFinishEventListenerSupplier.get());

    applicationEventMulticaster.removeApplicationListener(autoAnalysisFinishEventSupplier.get());

    applicationEventMulticaster.removeApplicationListener(
        uniqueErrorAnalysisStartEventSupplier.get());
    applicationEventMulticaster.removeApplicationListener(
        uniqueErrorAnalysisFinishEventSupplier.get());
  }

  @Override
  public Map<String, ?> getPluginParams() {
    Map<String, Object> params = new HashMap<>();
    params.put(ALLOWED_COMMANDS, new ArrayList<>(pluginCommandMapping.get().keySet()));
    params.put(COMMON_COMMANDS, new ArrayList<>(commonCommandsMapping.get().keySet()));
    return params;
  }

  @Override
  public CommonPluginCommand getCommonCommand(String commandName) {
    return commonCommandsMapping.get().get(commandName);
  }

  @Override
  public PluginCommand getIntegrationCommand(String commandName) {
    return pluginCommandMapping.get().get(commandName);
  }


  private Map<String, PluginCommand<?>> getCommands() {
    Map<String, PluginCommand<?>> pluginCommandMapping = new HashMap<>();
    final GetFileCommand getFileCommand = new GetFileCommand(resourcesDir,
        BINARY_DATA_PROPERTIES_FILE_ID);
    final TestConnectionCommand testConnectionCommand = new TestConnectionCommand();
    pluginCommandMapping.put(getFileCommand.getName(), getFileCommand);
    pluginCommandMapping.put(testConnectionCommand.getName(), testConnectionCommand);
    return pluginCommandMapping;
  }

  private Map<String, CommonPluginCommand<?>> getCommonCommandsMapping() {
    return Collections.emptyMap();
  }
}
