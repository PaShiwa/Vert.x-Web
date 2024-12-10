package com.pawan.learn_vertx.broker;

import com.pawan.learn_vertx.broker.config.ConfigLoader;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionInfoVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(VersionInfoVerticle.class);

  @Override
  public void start(final Promise<Void> startPromise) {
    ConfigLoader.load(vertx)
      .onFailure(startPromise::fail)
      .onSuccess(config->{
        logger.info(" Current Application Version is: {}",config.getVersion());
        startPromise.complete();
      });
  }
}
