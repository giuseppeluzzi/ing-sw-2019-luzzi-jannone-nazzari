package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Weapon;
import java.util.ArrayList;
import java.util.List;

public class EnemyWeaponUpdate implements Event {

  private static final long serialVersionUID = -60076482096933990L;
  private final PlayerColor enemyColor;
  private final int numWeapons;
  private final List<Weapon> unloadedWeapons;

  public EnemyWeaponUpdate(PlayerColor enemyColor, int numWeapons, List<Weapon> unloadedWeapons) {
    this.enemyColor = enemyColor;
    this.numWeapons = numWeapons;
    this.unloadedWeapons = new ArrayList<>();
    for (Weapon weapon : unloadedWeapons) {
      Weapon newWeapon = new Weapon(weapon.getCost(AmmoColor.RED), weapon.getCost(AmmoColor.BLUE), weapon.getCost(AmmoColor.YELLOW), weapon.getBaseCost(), weapon.getName());
      newWeapon.setLoaded(false);
      this.unloadedWeapons.add(newWeapon);
    }
  }

  public PlayerColor getEnemyColor() {
    return enemyColor;
  }

  public int getNumWeapons() {
    return numWeapons;
  }

  public List<Weapon> getUnloadedWeapons() {
    return new ArrayList<>(unloadedWeapons);
  }

  @Override
  public EventType getEventType() {
    return EventType.ENEMY_WEAPON_UPDATE;
  }
}
