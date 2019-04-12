package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.se2019.adrenalina.model.AmmoColor;
import it.polimi.se2019.adrenalina.model.Player;

public class SpawnPointDamageEvent implements Event {
  private final Player player;
  private final AmmoColor ammoColor;

  public SpawnPointDamageEvent(Player player, AmmoColor ammoColor) {
    this.player = player;
    this.ammoColor = ammoColor;
  }

  @Override
  public SpawnPointDamageEvent deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, SpawnPointDamageEvent.class);
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType", EventType.SPAWN_POINT_DAMAGE_EVENT.toString());
    return gson.toJson(jsonElement);
  }

  public Player getPlayer() {
    return player;
  }

  public AmmoColor getAmmoColor() {
    return ammoColor;
  }
}
