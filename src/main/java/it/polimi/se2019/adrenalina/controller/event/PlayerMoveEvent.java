package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;

public class PlayerMoveEvent implements Event {

  private static final long serialVersionUID = -5431170891646574471L;
  private final PlayerColor player;
  private final int squareX;
  private final int squareY;

  public PlayerMoveEvent(PlayerColor player, int squareX, int squareY) {
    this.player = player;
    this.squareX = squareX;
    this.squareY = squareY;
  }

  public PlayerColor getPlayer() {
    return player;
  }

  public int getSquareX() {
    return squareX;
  }

  public int getSquareY() {
    return squareY;
  }
}
