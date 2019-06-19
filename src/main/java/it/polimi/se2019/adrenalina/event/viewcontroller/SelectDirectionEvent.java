package it.polimi.se2019.adrenalina.event.viewcontroller;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Direction;

/**
 * Event fired when a player chooses a direction.
 */
public class SelectDirectionEvent implements Event {

  private static final long serialVersionUID = -2997596525218009549L;
  private final PlayerColor playerColor;
  private final Direction selectedDirection;

  public SelectDirectionEvent(PlayerColor player,
      Direction selectedDirection) {
    playerColor = player;
    this.selectedDirection = selectedDirection;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public Direction getSelectedDirection() {
    return selectedDirection;
  }

  @Override
  public EventType getEventType() {
    return EventType.SELECT_DIRECTION_EVENT;
  }
}
