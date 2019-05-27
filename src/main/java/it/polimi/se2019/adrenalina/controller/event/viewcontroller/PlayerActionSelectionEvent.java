package it.polimi.se2019.adrenalina.controller.event.viewcontroller;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.EventType;

/**
 * Event fired when a player choose his turn action
 */
public class PlayerActionSelectionEvent implements Event {

  private static final long serialVersionUID = -2788134306042770302L;
  private final PlayerColor playerColor;
  private final TurnAction turnAction;

  public PlayerActionSelectionEvent(PlayerColor playerColor, TurnAction turnAction) {
    this.playerColor = playerColor;
    this.turnAction  = turnAction;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public TurnAction getTurnAction() {
    return turnAction;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_ACTION_SELECTION_EVENT;
  }
}