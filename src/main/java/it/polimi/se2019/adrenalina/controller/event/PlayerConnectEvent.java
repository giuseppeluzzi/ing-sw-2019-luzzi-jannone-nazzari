package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class PlayerConnectEvent implements Event {
  private final String playerName;
  private final boolean domination;

  public PlayerConnectEvent(String playerName, boolean domination) {
    this.playerName = playerName;
    this.domination = domination;
  }

  public String getPlayerName() {
    return playerName;
  }

  public boolean isDomination() {
    return domination;
  }

  @Override
  public PlayerConnectEvent deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, PlayerConnectEvent.class);
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType", EventType.PLAYER_CONNECT_EVENT.toString());
    return gson.toJson(jsonElement);
  }
}
