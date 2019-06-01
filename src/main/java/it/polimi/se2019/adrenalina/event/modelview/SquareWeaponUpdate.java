package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Weapon;
import java.util.ArrayList;
import java.util.List;

public class SquareWeaponUpdate implements Event {

  private static final long serialVersionUID = -777315149155987994L;
  private final List<Weapon> weapons = new ArrayList<>();
  private final int posX;
  private final int posY;

  public SquareWeaponUpdate(int posX, int posY, List<Weapon> weapons) {
    for (Weapon weapon : weapons) {
      this.weapons.add(new Weapon(weapon.getCost(AmmoColor.RED), weapon.getCost(AmmoColor.BLUE), weapon.getCost(AmmoColor.YELLOW), weapon.getBaseCost(), weapon.getName(), weapon.getSymbol()));
    }
    this.posX = posX;
    this.posY = posY;
  }

  public List<Weapon> getWeapons() {
    return new ArrayList<>(weapons);
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
