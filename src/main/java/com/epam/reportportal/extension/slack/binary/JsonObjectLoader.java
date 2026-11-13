package com.epam.reportportal.extension.slack.binary;

import java.io.File;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

public class JsonObjectLoader {

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonObjectLoader.class);

  private final JsonMapper jsonMapper;

  public JsonObjectLoader(JsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  public <T> Optional<T> load(File file, Class<T> clazz) {
    try {
      return Optional.ofNullable(jsonMapper.readValue(file, clazz));
    } catch (JacksonException e) {
      LOGGER.error(e.getMessage(), e);
      return Optional.empty();
    }
  }

}
