package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import java.util.HashMap;

public class SquareWeaponUpdate implements Event {

  private static final long serialVersionUID = -777315149155987994L;
  private final HashMap<String, HashMap<AmmoColor, Integer>> weapons;
  private final int posX;
  private final int posY;

  public SquareWeaponUpdate(int posX, int posY,
      HashMap<String, HashMap<AmmoColor, Integer>> weapons) {
    this.weapons = new HashMap<>(weapons);
    this.posX = posX;
    this.posY = posY;
  }

  public HashMap<String, HashMap<AmmoColor, Integer>> getWeapons() {
    return new HashMap<>(weapons);
  }

  public int getPosX() {
    return posX;
  }

  public int getPosY() {
    return posY;
  }

  @Override
  public EventType getEventType() {
    return EventType.SQUARE_WEAPON_UPDATE;
  }
}
