package it.polimi.se2019.adrenalina.model;

import java.util.EnumMap;

public class AmmoCard {
  private final EnumMap<AmmoColor, Integer> ammo;
  private final int powerUp;

  public AmmoCard(int red, int blue, int yellow, int powerUp) {
    this.powerUp = powerUp;
    ammo = new EnumMap<>(AmmoColor.class);
    ammo.put(AmmoColor.RED, red);
    ammo.put(AmmoColor.BLUE, blue);
    ammo.put(AmmoColor.YELLOW, yellow);
  }

  public AmmoCard(AmmoCard ammoCard) {
    powerUp = ammoCard.powerUp;
    ammo = new EnumMap<>(AmmoColor.class);
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
}
