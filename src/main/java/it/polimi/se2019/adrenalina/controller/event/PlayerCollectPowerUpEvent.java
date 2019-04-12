package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;

public class PlayerCollectPowerUpEvent implements Event{
  private final Player player;
  private final PowerUp powerUp;

  public PlayerCollectPowerUpEvent(Player player, PowerUp powerUp) {
    this.player = player;
    this.powerUp = powerUp;
  }

  @Override
  public PlayerCollectPowerUpEvent deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, PlayerCollectPowerUpEvent.class);
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType", EventType.PLAYER_COLLECT_POWERUP_EVENT.toString());
    return gson.toJson(jsonElement);
  }

  public Player getPlayer() {
    return player;
  }

  public PowerUp getPowerUp() {
    return powerUp;
  }
}
