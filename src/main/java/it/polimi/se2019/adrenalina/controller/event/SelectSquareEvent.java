package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;

public class SelectSquareEvent implements Event {

  private static final long serialVersionUID = 6690751482225846610L;
  private final PlayerColor playerColor;
  private final int squareX;
  private final int squareY;

  public SelectSquareEvent(PlayerColor playerColor, int squareX, int squareY) {
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

  @Override
  public EventType getEventType() {
    return EventType.SELECT_SQUARE_EVENT;
  }
}
