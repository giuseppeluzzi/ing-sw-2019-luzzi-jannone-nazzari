package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Event sent when a player is removed from the game board.
 */
public class BoardRemovePlayerUpdate implements Event {

  private static final long serialVersionUID = -7533996492008845143L;
  private final PlayerColor playerColor;

  public BoardRemovePlayerUpdate(PlayerColor playerColor) {
    this.playerColor = playerColor;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public EventType getEventType() {
    return EventType.BOARD_REMOVE_PLAYER_UPDATE;
  }
}
