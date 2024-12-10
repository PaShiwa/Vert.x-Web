package com.pawan.learn_vertx.broker.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;

public class ConfigLoader {

  private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
  public static final String SERVER_PORT = "SERVER_PORT";
  static final List<String> EXPOSED_ENVIRONMENT_VARIABLES= Arrays.asList(SERVER_PORT);
  public static final String ConFIG_FILE = "application.yaml";

  public static Future<BrokerConfig> load (Vertx vertx){
    var exposedKeys = new JsonArray();
    EXPOSED_ENVIRONMENT_VARIABLES.forEach(exposedKeys::add);
    logger.debug("Fetch configuration for {}", exposedKeys.encode());

    var envStore = new ConfigStoreOptions()
      .setType("env")
      .setConfig(new JsonObject().put("keys", exposedKeys));

    var propertyStore = new ConfigStoreOptions()
      .setType("sys")
      .setConfig(new JsonObject().put("cache", false));

    var yamlStore = new ConfigStoreOptions()
      .setType("file")
      .setFormat("yaml")
      .setConfig(new JsonObject().put("path", ConFIG_FILE));

    var retriever= ConfigRetriever.create(vertx,
      new ConfigRetrieverOptions()
        // Priority increases downwards in overloading
        .addStore(yamlStore)
        .addStore(envStore)
        .addStore(propertyStore)
      );
    return retriever.getConfig().map(BrokerConfig::from);
  }
}
