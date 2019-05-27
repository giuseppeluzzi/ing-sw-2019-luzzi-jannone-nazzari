package it.polimi.se2019.adrenalina.event.viewcontroller;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Event fired when a player selects a square as a target during a shooting
 */
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
