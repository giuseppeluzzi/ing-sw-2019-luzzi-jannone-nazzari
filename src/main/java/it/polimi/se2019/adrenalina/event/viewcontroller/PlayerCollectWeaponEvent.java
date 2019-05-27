package it.polimi.se2019.adrenalina.event.viewcontroller;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Event fired when a player fetches a weapon from a spawnpoint
 */
public class PlayerCollectWeaponEvent implements Event {

  private static final long serialVersionUID = -4228677364669070987L;
  private final PlayerColor playerColor;
  private final String weaponName;

  public PlayerCollectWeaponEvent(PlayerColor playerColor, String weaponName) {
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
    return EventType.PLAYER_COLLECT_WEAPON_EVENT;
  }
}
