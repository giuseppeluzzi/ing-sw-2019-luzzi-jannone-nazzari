package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;

public class PlayerMoveEvent extends PlayerSquareEvent {

  private static final long serialVersionUID = -7629325377169943283L;

  protected PlayerMoveEvent(PlayerColor playerColor,
      int squareX, int squareY) {
    super(playerColor, squareX, squareY);
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_MOVE_EVENT;
  }
}
