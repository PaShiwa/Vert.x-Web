package com.pawan.learn_vertx.broker;
import com.pawan.learn_vertx.broker.config.ConfigLoader;
import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    //System.setProperty(ConfigLoader.SERVER_PORT,"9000");
    var vertx = Vertx.vertx();
    vertx.exceptionHandler(error->{
      logger.error("Unhandled: ", error);
    });
    vertx.deployVerticle(new MainVerticle())
      .onFailure(err->logger.error("Failed to deploy: ", err))
      .onSuccess(id->{
          logger.info("Deployed {} with id {} !",MainVerticle.class.getName(), id);
        });
}

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.deployVerticle(VersionInfoVerticle.class.getName())
        .onFailure(startPromise::fail)
        .onSuccess(id -> {logger.info("Deployed {} with id {}", VersionInfoVerticle.class.getName(), id);})
        .compose(next-> deployRestAPIVerticle(startPromise));
  }

  private Future<String> deployRestAPIVerticle(Promise<Void> startPromise) {
    return vertx.deployVerticle(RestApiVerticle.class.getName(),
        new DeploymentOptions().setInstances(getAvailableProcessors())
      )
      .onFailure(startPromise::fail)
      .onSuccess(id -> {
        logger.info("Deployed {} with id {}", RestApiVerticle.class.getName(), id);
        startPromise.complete();
      });
  }

  private static int getAvailableProcessors() {

    return Math.max(1, Runtime.getRuntime().availableProcessors()/2);
  }
}
