package com.pawan.learn_vertx.broker;

import com.pawan.learn_vertx.broker.assets.AssetsRestApi;
import com.pawan.learn_vertx.broker.config.BrokerConfig;
import com.pawan.learn_vertx.broker.config.ConfigLoader;
import com.pawan.learn_vertx.broker.quotes.QuotesRestApi;
import com.pawan.learn_vertx.broker.watchlist.WatchListRestApi;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestApiVerticle extends AbstractVerticle {
  static Logger logger = LoggerFactory.getLogger(RestApiVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    ConfigLoader.load(vertx)
        .onFailure(startPromise::fail)
        .onSuccess(config->{
            logger.info("Retrieved Configuration {}",config);
            startHttpServerAndAttachRoutes(startPromise, config);
        });
  }
    private void startHttpServerAndAttachRoutes(Promise<Void> startPromise, BrokerConfig configuration) {
      final Router restApi = Router.router(vertx);
      restApi.route()
        .handler(BodyHandler.create())
        .failureHandler(handleFailure());

      AssetsRestApi.attach(restApi);
      QuotesRestApi.attach(restApi);
      WatchListRestApi.attach(restApi);

      vertx.createHttpServer()
        .requestHandler(restApi)
        .exceptionHandler(error->{
          logger.error("HTTP Server Error:", error);
        })
        .listen(configuration.getServerPort())
        .onComplete(http -> {
          if (http.succeeded()) {
            startPromise.complete();
            logger.info("HTTP server started on port {}", configuration.getServerPort());
          } else {
            startPromise.fail(http.cause());
          }
        });
    }

    private static Handler<RoutingContext> handleFailure() {
      return errorContext -> {
        if (errorContext.response().ended()) {
          return;
        }
        logger.error("Router error!", errorContext.failure());
        errorContext.response()
          .setStatusCode(500)
          .end(new JsonObject().put("Message", "Something went wrong!").toBuffer());
      };
    }
  }
