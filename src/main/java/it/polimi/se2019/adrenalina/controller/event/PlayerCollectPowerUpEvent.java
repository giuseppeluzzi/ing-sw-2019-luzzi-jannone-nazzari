package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import java.io.Serializable;

public class PlayerCollectPowerUpEvent implements Event, Serializable {
  private final Player player;
  private final PowerUp powerUp;

  public PlayerCollectPowerUpEvent(Player player, PowerUp powerUp) {
    this.player = player;
    this.powerUp = powerUp;
  }

  public static PlayerCollectPowerUpEvent deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
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
