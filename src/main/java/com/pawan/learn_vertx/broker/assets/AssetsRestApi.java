package com.pawan.learn_vertx.broker.assets;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class AssetsRestApi {
  private static final Logger logger = LoggerFactory.getLogger(AssetsRestApi.class);
  public static final List<String> ASSETS = Arrays.asList("AAPL", "AMZN", "NFLX","TSLA", "FB", "MSFT", "GOOG");
  public static void attach(Router restApi) {
    restApi.get("/assets").handler(new GetAssetHandler());
  }
}
