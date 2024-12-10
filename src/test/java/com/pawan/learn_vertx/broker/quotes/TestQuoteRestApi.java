package com.pawan.learn_vertx.broker.quotes;

import com.pawan.learn_vertx.broker.AbstractRestApiTest;
import com.pawan.learn_vertx.broker.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestQuoteRestApi extends AbstractRestApiTest {
  private static final Logger logger = LoggerFactory.getLogger(TestQuoteRestApi.class);

  @Test
  void returns_quote_for_assets(Vertx vertx, VertxTestContext context) throws Throwable {
    var client = getWebClient(vertx);
    client.get("/quotes/AMZN")
        .send()
          .onComplete(context.succeeding(response->{
           var jsonObject= response.bodyAsJsonObject();
           logger.info("Response: {}", jsonObject);
           assertEquals("{\"company\":\"AMZN\"}", jsonObject.getJsonObject("asset").encode());
           assertEquals(200, response.statusCode());
            context.completeNow();
          }));
  }

  @Test
  void unknown_asset(Vertx vertx, VertxTestContext context) throws Throwable {
    var client =getWebClient(vertx);
    client.get("/quotes/UNKNOWN")
      .send()
      .onComplete(context.succeeding(response->{
        var jsonObject= response.bodyAsJsonObject();
        logger.info("Response: {}", jsonObject);
        assertEquals("{\"Message:\":\"Quote for asset UNKNOWN not found!\",\"path \":\"/quotes/UNKNOWN\"}", jsonObject.encode());
        assertEquals(404, response.statusCode());
        context.completeNow();
      }));
  }

  private static WebClient getWebClient(Vertx vertx) {
    return WebClient.create(vertx, new WebClientOptions().setDefaultPort(TEST_SERVER_PORT));
  }
}
