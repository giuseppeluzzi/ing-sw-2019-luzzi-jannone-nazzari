package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.se2019.adrenalina.model.Player;

public class PlayerDeathEvent implements Event {
  private final Player player;
  private final Player killer;

  public PlayerDeathEvent(Player player, Player killer) {
    this.player = player;
    this.killer = killer;
  }

  @Override
  public PlayerDeathEvent deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, PlayerDeathEvent.class);
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType", EventType.PLAYER_DEATH_EVENT.toString());
    return gson.toJson(jsonElement);
  }

  public Player getPlayer() {
    return player;
  }

  public Player getKiller() {
    return killer;
  }
}
