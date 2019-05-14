package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import java.io.Serializable;

public class PlayerCollectWeaponEvent implements Event {

  private static final long serialVersionUID = -4228677364669070987L;
  private final PlayerColor playerColor;
  private final String weaponName;
  private final int squareX;
  private final int squareY;

  public PlayerCollectWeaponEvent(PlayerColor playerColor, String weaponName, int squareX,
      int squareY) {
    this.playerColor = playerColor;
    this.weaponName = weaponName;
    this.squareX = squareX;
    this.squareY = squareY;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public String getWeaponName() {
    return weaponName;
  }

  public int getSquareX() {
    return squareX;
  }

  public int getSquareY() {
    return squareY;
  }
}
