package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;

public class WeaponUpdateEvent implements Event {
  private final Square square;
  private final Weapon weapon;
  private final boolean remove;

  public WeaponUpdateEvent(Square square, Weapon weapon, boolean remove) {
    this.square = square;
    this.weapon = weapon;
    this.remove = remove;
  }

  @Override
  public WeaponUpdateEvent deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, WeaponUpdateEvent.class);
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType", EventType.WEAPON_UPDATE_EVENT.toString());
    return gson.toJson(jsonElement);
  }

  public Square getSquare() {
    return square;
  }

  public Weapon getWeapon() {
    return weapon;
  }

  public boolean isRemove() {
    return remove;
  }
}
