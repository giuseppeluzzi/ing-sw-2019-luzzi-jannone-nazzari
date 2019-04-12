package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;

public class PlayerCollectAmmoEvent implements Event {
  private final Player player;
  private final Square square;

  public PlayerCollectAmmoEvent(Player player, Square square) {
    this.player = player;
    this.square = square;
  }

  @Override
  public PlayerCollectAmmoEvent deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, PlayerCollectAmmoEvent.class);
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType", EventType.PLAYER_COLLECT_AMMO_EVENT.toString());
    return gson.toJson(jsonElement);
  }

  public Player getPlayer() {
    return player;
  }

  public Square getSquare() {
    return square;
  }
}
