package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;

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
}