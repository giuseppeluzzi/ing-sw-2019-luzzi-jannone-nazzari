package it.polimi.se2019.adrenalina.event.viewcontroller;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Event fired when a player swaps his weapons for another one.
 */
public class PlayerSwapWeaponEvent implements Event {

  private static final long serialVersionUID = 8252298742197699384L;
  private final PlayerColor playerColor;
  private final String ownWeaponName;
  private final String squareWeaponName;

  public PlayerSwapWeaponEvent(PlayerColor playerColor, String ownWeaponName,
      String squareWeaponName) {
    this.playerColor = playerColor;
    this.ownWeaponName = ownWeaponName;
    this.squareWeaponName = squareWeaponName;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public String getOwnWeaponName() {
    return ownWeaponName;
  }

  public String getSquareWeaponName() {
    return squareWeaponName;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_SWAP_WEAPON_EVENT;
  }
}
