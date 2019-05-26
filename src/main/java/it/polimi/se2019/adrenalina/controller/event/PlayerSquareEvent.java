package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;

public abstract class PlayerSquareEvent implements Event {

  private static final long serialVersionUID = -3420489819986950040L;
  private final PlayerColor playerColor;
  private final int squareX;
  private final int squareY;

  protected PlayerSquareEvent(PlayerColor playerColor, int squareX, int squareY) {
    this.playerColor = playerColor;
    this.squareX = squareX;
    this.squareY = squareY;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public int getSquareX() {
    return squareX;
  }

  public int getSquareY() {
    return squareY;
  }
}
