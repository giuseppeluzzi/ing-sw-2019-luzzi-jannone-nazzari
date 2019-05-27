package it.polimi.se2019.adrenalina.controller.event.viewcontroller;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.EventType;

/**
 * Event fired when a player fetch an ammo card from a square
 */
public class PlayerCollectAmmoEvent implements Event {

  private static final long serialVersionUID = 6231804188928285372L;
  private final PlayerColor playerColor;
  private final int squareX;
  private final int squareY;

  public PlayerCollectAmmoEvent(PlayerColor playerColor, int squareX, int squareY) {
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
    return EventType.PLAYER_COLLECT_AMMO_EVENT;
  }
}
