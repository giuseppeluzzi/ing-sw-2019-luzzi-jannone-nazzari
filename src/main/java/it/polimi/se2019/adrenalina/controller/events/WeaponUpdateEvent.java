package it.polimi.se2019.adrenalina.controller.events;

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
