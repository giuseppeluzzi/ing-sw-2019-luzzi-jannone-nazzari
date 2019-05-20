package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;

public class PlayerSelectWeaponEffectEvent implements Event {

  private static final long serialVersionUID = 6708580194992758891L;
  private final PlayerColor playerColor;
  private final String weaponName;
  private final String effectName;

  public PlayerSelectWeaponEffectEvent(PlayerColor playerColor, String weaponName, String effectName) {
    this.playerColor = playerColor;
    this.weaponName = weaponName;
    this.effectName = effectName;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public String getWeaponName() {
    return weaponName;
  }

  public String getEffectName() {
    return effectName;
  }
}