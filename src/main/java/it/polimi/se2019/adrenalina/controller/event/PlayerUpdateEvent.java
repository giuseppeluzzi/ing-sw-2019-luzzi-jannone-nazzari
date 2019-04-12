package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.se2019.adrenalina.model.Player;

public class PlayerUpdateEvent implements Event {
  private final Player player;

  public PlayerUpdateEvent(Player player) {
    this.player = player;
  }

  @Override
  public PlayerUpdateEvent deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, PlayerUpdateEvent.class);
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType", EventType.PLAYER_UPDATE_EVENT.toString());
    return gson.toJson(jsonElement);
  }

  public Player getPlayer() {
    return player;
  }
}
