package it.polimi.se2019.adrenalina.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;

public class PlayerSpawnEvent extends PlayerSquareEvent {

  private static final long serialVersionUID = -2633375389689666256L;

  protected PlayerSpawnEvent(PlayerColor playerColor,
      int squareX, int squareY) {
    super(playerColor, squareX, squareY);
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_SPAWN_EVENT;
  }
}
