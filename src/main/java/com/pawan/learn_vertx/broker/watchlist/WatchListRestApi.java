package com.pawan.learn_vertx.broker.watchlist;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class WatchListRestApi {
  private static final Logger logger= LoggerFactory.getLogger(WatchListRestApi.class);
  public static void attach(Router restApi) {
    final HashMap<UUID, WatchList> watchListPerAccount = new HashMap<UUID, WatchList>();
    final String path = "/account/watchlist/:accountId";

    restApi.get(path).handler(new GetWatchListHandler(watchListPerAccount));

    restApi.put(path).handler(new PutWatchListHandler(watchListPerAccount));

    restApi.delete(path).handler(new DeleteWatchListHandler(watchListPerAccount));
  }
  public static String getAccountId(RoutingContext context) {
    var accountId = context.pathParam("accountId");
    logger.debug("{} for account {}", context.normalizedPath(),accountId);
    return accountId;
  }
}
