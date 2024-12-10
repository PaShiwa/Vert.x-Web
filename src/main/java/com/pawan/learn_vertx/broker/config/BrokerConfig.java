package com.pawan.learn_vertx.broker.config;

import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import java.util.Objects;

@Builder
@Value
@ToString
public class BrokerConfig {
  int serverPort;
  String version;
  public  static BrokerConfig from(JsonObject config){
    final Integer serverPort= config.getInteger(ConfigLoader.SERVER_PORT);
    if(Objects.isNull(serverPort)){
      throw new RuntimeException(ConfigLoader.SERVER_PORT + "not configured!");
    }

     final String version = config.getString("version");
    if(Objects.isNull(version)){
      throw new RuntimeException("Version not configured in yaml file!");
    }

    return BrokerConfig.builder()
      .serverPort(serverPort)
      .version(version)
      .build();
  }
}
