package com.pawan.learn_vertx.broker.assets;

import com.pawan.learn_vertx.broker.AbstractRestApiTest;
import com.pawan.learn_vertx.broker.MainVerticle;
import com.pawan.learn_vertx.broker.config.ConfigLoader;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
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
public class TestAssetRestApi extends AbstractRestApiTest {
  private static final Logger logger = LoggerFactory.getLogger(TestAssetRestApi.class);

  @Test
  void returns_all_assets(Vertx vertx, VertxTestContext context) throws Throwable {
    var client =WebClient.create(vertx, new WebClientOptions().setDefaultPort(TEST_SERVER_PORT));
    client.get("/assets")
        .send()
          .onComplete(context.succeeding(response->{
           var jsonArray= response.bodyAsJsonArray();
           logger.info("Response: {}", jsonArray);
           assertEquals("[{\"company\":\"AAPL\"},{\"company\":\"AMZN\"},{\"company\":\"NFLX\"},{\"company\":\"TSLA\"},{\"company\":\"FB\"},{\"company\":\"MSFT\"},{\"company\":\"GOOG\"}]", jsonArray.encode());
           assertEquals(200, response.statusCode());
           assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(), response.getHeader(HttpHeaders.CONTENT_TYPE));
           assertEquals("my-value", response.getHeader("my-header"));
            context.completeNow();
          }));
  }
}
