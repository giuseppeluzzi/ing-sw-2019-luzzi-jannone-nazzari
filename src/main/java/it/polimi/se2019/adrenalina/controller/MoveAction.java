package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.model.ActionType;

public class MoveAction implements Action{
  private final int target;
  private final int destination;
  private final ActionType type;

  public MoveAction(int target, int destination) {
    this.target = target;
    this.destination = destination;
    type = ActionType.MOVE;
  }

  @Override
  public ActionType getActionType() {
    return type;
  }

  public int getTarget() {
    return target;
  }

  public int getDestination() {
    return destination;
  }
}
