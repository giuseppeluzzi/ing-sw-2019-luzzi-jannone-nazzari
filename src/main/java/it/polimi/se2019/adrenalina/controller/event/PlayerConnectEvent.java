package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;

public class PlayerConnectEvent {
  private final EventType type = EventType.PLAYER_CONNECT_EVENT;
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

  public String serialize() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public static PlayerConnectEvent deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, PlayerConnectEvent.class);
  }
}
