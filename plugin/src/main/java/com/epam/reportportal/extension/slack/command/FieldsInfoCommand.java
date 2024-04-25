package com.epam.reportportal.extension.slack.command;

import com.epam.reportportal.extension.PluginCommand;
import com.epam.ta.reportportal.entity.integration.Integration;

import java.util.List;
import java.util.Map;

/**
 * @author Andrei Piankouski
 */
public class FieldsInfoCommand implements PluginCommand<List<String>> {

    @Override
    public String getName() {
        return "FieldsInfoCommand";
    }

    @Override
    public List<String> executeCommand(Integration integration, Map<String, Object> params) {
        return List.of("webhook_url");
    }
}
