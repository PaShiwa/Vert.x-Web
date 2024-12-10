package com.pawan.learn_vertx.broker.watchlist;

import ch.qos.logback.core.testUtil.XTeeOutputStream;
import com.pawan.learn_vertx.broker.AbstractRestApiTest;
import com.pawan.learn_vertx.broker.MainVerticle;
import com.pawan.learn_vertx.broker.assets.Asset;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestWatchListRestApi extends AbstractRestApiTest {
  private static final Logger logger = LoggerFactory.getLogger(TestWatchListRestApi.class);

  @Test
  void adds_and_returns_watchLlist_for_accout(Vertx vertx, VertxTestContext context) throws Throwable {
    var client = getWebClient(vertx);
    var accountID = UUID.randomUUID();
    client.put("/account/watchlist/"+ accountID)
        .sendJsonObject(requestBody())
          .onComplete(context.succeeding(response->{
           var jsonObject= response.bodyAsJsonObject();
           logger.info("Response Put: {}", jsonObject);
           assertEquals("{\"assetList\":[{\"company\":\"AMZN\"},{\"company\":\"TSLA\"}]}", jsonObject.encode());
           assertEquals(200, response.statusCode());
          }))
      .compose(next->{
        client.get("/account/watchlist/" + accountID)
          .send()
          .onComplete(context.succeeding(response ->{
            var jsonObject = response.bodyAsJsonObject();
            logger.info("Response Get: {}", jsonObject);
            assertEquals("{\"assetList\":[{\"company\":\"AMZN\"},{\"company\":\"TSLA\"}]}", jsonObject.encode());
            assertEquals(200, response.statusCode());
            context.completeNow();
          }));
            return Future.succeededFuture();
      });
  }


  @Test
  void adds_deletes_watchListPerAccount(Vertx vertx, VertxTestContext context) {
    var client = getWebClient(vertx);
    var accountID = UUID.randomUUID();

    client.put("/account/watchlist/" + accountID)
      .sendJsonObject(requestBody())
      .onComplete(context.succeeding(response -> {
        var jsonObject = response.bodyAsJsonObject();
        logger.info("Response Put: {}", jsonObject);
        assertEquals("{\"assetList\":[{\"company\":\"AMZN\"},{\"company\":\"TSLA\"}]}", jsonObject.encode());
        assertEquals(200, response.statusCode());
      }))
      .compose(next -> {
        client.delete("/account/watchlist/" + accountID)
          .send()
          .onComplete(context.succeeding(response -> {
            var jsonObject = response.bodyAsJsonObject();
            logger.debug("Response Delete: {}", jsonObject);
            assertEquals("{\"assetList\":[{\"company\":\"AMZN\"},{\"company\":\"TSLA\"}]}", jsonObject.encode());
            assertEquals(200, response.statusCode());
            context.completeNow();
          }));
        return Future.succeededFuture();
      });
  }

  private static WebClient getWebClient(Vertx vertx) {
    return WebClient.create(vertx, new WebClientOptions().setDefaultPort(TEST_SERVER_PORT));
  }
  private static JsonObject requestBody() {
    return new WatchList(Arrays.asList(
      new Asset("AMZN"),
      new Asset("TSLA")))
      .toJsonObject();
  }
}
