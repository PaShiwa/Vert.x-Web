package com.pawan.learn_vertx.broker.assets;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.pawan.learn_vertx.broker.assets.AssetsRestApi.ASSETS;

public class GetAssetHandler implements Handler<RoutingContext> {
  Logger logger = LoggerFactory.getLogger(GetAssetHandler.class);
  @Override
  public void handle(RoutingContext routingContext) {
    final JsonArray response = new JsonArray();
    ASSETS.stream().map(Asset::new).forEach(response::add);
    logger.info("Path '{}' responds with {}", routingContext.normalizedPath(), response.encode());
    routingContext.response()
      .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
      .putHeader("my-header", "my-value")
      .end(response.toBuffer());
  }
  }
