package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import java.util.ArrayList;
import java.util.List;

public class EnemyWeaponUpdate implements Event {

  private static final long serialVersionUID = -60076482096933990L;
  private final PlayerColor enemyColor;
  private final int numWeapons;
  private final List<String> unloadedWeapons;

  public EnemyWeaponUpdate(PlayerColor enemyColor, int numWeapons, List<String> unloadedWeapons) {
    this.enemyColor = enemyColor;
    this.numWeapons = numWeapons;
    this.unloadedWeapons = new ArrayList<>(unloadedWeapons);
  }

  public PlayerColor getEnemyColor() {
    return enemyColor;
  }

  public int getNumWeapons() {
    return numWeapons;
  }

  public List<String> getUnloadedWeapons() {
    return new ArrayList<>(unloadedWeapons);
  }

  @Override
  public EventType getEventType() {
    return EventType.ENEMY_WEAPON_UPDATE;
  }
}
