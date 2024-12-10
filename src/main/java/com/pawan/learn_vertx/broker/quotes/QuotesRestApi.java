package com.pawan.learn_vertx.broker.quotes;

import com.pawan.learn_vertx.broker.assets.Asset;
import com.pawan.learn_vertx.broker.assets.AssetsRestApi;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class QuotesRestApi {
  private static final Logger logger = LoggerFactory.getLogger(QuotesRestApi.class);
  public static void attach(Router restApi) {
    final Map<String,Quote> cachedQuotes = new HashMap<>();
    AssetsRestApi.ASSETS.forEach(asset->{
      cachedQuotes.put(asset, initRandomQuote(asset));
    });

    restApi.get("/quotes/:asset").handler(new GetQuoteHandler(cachedQuotes));
  }

  private static Quote initRandomQuote(String assetParam) {
    return Quote.builder()
      .asset(new Asset(assetParam))
      .ask(randomValue())
      .volume(randomValue())
      .bid(randomValue())
      .lastPrice(randomValue())
      .volume(randomValue())
      .build();
  }

  private static BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1,100));
  }
}
