package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;

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
