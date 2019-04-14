package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Square;

public class PlayerSpawnEvent implements Event {
  private final Player player;
  private final Square spawnLocation;
  private final PowerUp tossedPowerUp;

  public PlayerSpawnEvent(Player player, Square spawnLocation,
      PowerUp tossedPowerUp) {
    this.player = player;
    this.spawnLocation = spawnLocation;
    this.tossedPowerUp = tossedPowerUp;
  }

  public static PlayerSpawnEvent deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, PlayerSpawnEvent.class);
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType", EventType.PLAYER_SPAWN_EVENT.toString());
    return gson.toJson(jsonElement);
  }

  public Player getPlayer() {
    return player;
  }

  public Square getSpawnLocation() {
    return spawnLocation;
  }

  public PowerUp getTossedPowerUp() {
    return tossedPowerUp;
  }
}
