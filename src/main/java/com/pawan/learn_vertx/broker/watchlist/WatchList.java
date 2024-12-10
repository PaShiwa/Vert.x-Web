package com.pawan.learn_vertx.broker.watchlist;

import com.pawan.learn_vertx.broker.assets.Asset;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchList {
  private List<Asset> assetList;

  JsonObject toJsonObject(){
    return JsonObject.mapFrom(this);
  }
}

