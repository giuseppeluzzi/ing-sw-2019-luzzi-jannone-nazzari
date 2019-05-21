package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;

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
}