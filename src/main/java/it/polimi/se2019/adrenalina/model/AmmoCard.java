package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import java.util.HashMap;

public class AmmoCard {
  private final HashMap<AmmoColor, Integer> ammo;
  private final int powerUp;

  public AmmoCard(int red, int blue, int yellow, int powerUp) {
    // TODO: why AmmoColor.class was an argument of HashMap<>()?
    this.powerUp = powerUp;
    ammo = new HashMap<>();
    ammo.put(AmmoColor.RED, red);
    ammo.put(AmmoColor.BLUE, blue);
    ammo.put(AmmoColor.YELLOW, yellow);
  }

  public AmmoCard(AmmoCard ammoCard) {
    powerUp = ammoCard.powerUp;
    ammo = new HashMap<>();
    ammo.put(AmmoColor.RED, ammoCard.ammo.get(AmmoColor.RED));
    ammo.put(AmmoColor.BLUE, ammoCard.ammo.get(AmmoColor.BLUE));
    ammo.put(AmmoColor.YELLOW, ammoCard.ammo.get(AmmoColor.YELLOW));
  }

  public int getAmmo(AmmoColor color) {
    return ammo.get(color);
  }

  public int getPowerUp() {
    return powerUp;
  }

  public String serialize() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public AmmoCard deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, AmmoCard.class);
  }
}
