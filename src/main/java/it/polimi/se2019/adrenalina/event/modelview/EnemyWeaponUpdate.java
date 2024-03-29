package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Weapon;

import java.util.ArrayList;
import java.util.List;

/**
 * Event sent when the number or status of weapons of a player changes. This event is used by the player's
 * enemies who cannot see what the actual loaded weapons are.
 */
public class EnemyWeaponUpdate implements Event {

  private static final long serialVersionUID = -60076482096933990L;
  private final PlayerColor playerColor;
  private final int numWeapons;
  private final List<Weapon> unloadedWeapons;

  public EnemyWeaponUpdate(PlayerColor playerColor, int numWeapons, List<Weapon> unloadedWeapons) {
    this.playerColor = playerColor;
    this.numWeapons = numWeapons;
    this.unloadedWeapons = new ArrayList<>(unloadedWeapons);
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
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
