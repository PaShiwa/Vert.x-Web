package com.pawan.learn_vertx.broker.watchlist;

import io.netty.buffer.CompositeByteBuf;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.UUID;

import static com.pawan.learn_vertx.broker.watchlist.WatchListRestApi.getAccountId;

public class DeleteWatchListHandler implements Handler <RoutingContext> {

  private final HashMap<UUID, WatchList> watchListPerAccount;

  public DeleteWatchListHandler(HashMap<UUID, WatchList> watchListPerAccount) {
    this.watchListPerAccount = watchListPerAccount;
  }

  @Override
  public void handle(RoutingContext context) {
    var accountId = getAccountId(context);
    var watchList = watchListPerAccount.remove(UUID.fromString(accountId));
    var jsonObject = watchList.toJsonObject();
    context.response()
      .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
      .end(jsonObject.toBuffer());
  }
}
