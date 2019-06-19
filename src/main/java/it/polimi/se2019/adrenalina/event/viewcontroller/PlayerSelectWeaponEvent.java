package it.polimi.se2019.adrenalina.event.viewcontroller;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Event fired when a player selects a weapon for shooting.
 */
public class PlayerSelectWeaponEvent implements Event {

  private static final long serialVersionUID = 3088618111020779192L;
  private final PlayerColor playerColor;
  private final String weaponName;

  public PlayerSelectWeaponEvent(PlayerColor playerColor, String weaponName) {
    this.playerColor = playerColor;
    this.weaponName = weaponName;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public String getWeaponName() {
    return weaponName;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_SELECT_WEAPON_EVENT;
  }
}