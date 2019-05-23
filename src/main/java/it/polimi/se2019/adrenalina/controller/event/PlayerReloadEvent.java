package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;

public class PlayerReloadEvent implements Event {

  private static final long serialVersionUID = -2788134306042770302L;
  private final PlayerColor playerColor;
  private final String weaponName;

  public PlayerReloadEvent(PlayerColor playerColor, String weaponName) {
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
    return EventType.PLAYER_RELOAD_EVENT;
  }
}