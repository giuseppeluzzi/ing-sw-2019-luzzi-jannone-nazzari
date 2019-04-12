package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;

public class PlayerReloadEvent implements Event {
  private final Player player;
  private final Weapon weapon;

  public PlayerReloadEvent(Player player, Weapon weapon) {
    this.player = player;
    this.weapon = weapon;
  }


  @Override
  public PlayerReloadEvent deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, PlayerReloadEvent.class);
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType", EventType.PLAYER_RELOAD_EVENT.toString());
    return gson.toJson(jsonElement);
  }

  public Player getPlayer() {
    return player;
  }

  public Weapon getWeapon() {
    return weapon;
  }
}
