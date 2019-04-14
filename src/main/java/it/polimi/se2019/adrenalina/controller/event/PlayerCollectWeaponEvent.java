package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;

public class PlayerCollectWeaponEvent implements Event {
  private final Player player;
  private final Weapon weapon;
  private final Square square;

  public PlayerCollectWeaponEvent(Player player, Weapon weapon,
      Square square) {
    this.player = player;
    this.weapon = weapon;
    this.square = square;
  }

  public static PlayerCollectWeaponEvent deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, PlayerCollectWeaponEvent.class);
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType", EventType.PLAYER_COLLECT_WEAPON_EVENT.toString());
    return gson.toJson(jsonElement);
  }

  public Player getPlayer() {
    return player;
  }

  public Weapon getWeapon() {
    return weapon;
  }

  public Square getSquare() {
    return square;
  }
}
