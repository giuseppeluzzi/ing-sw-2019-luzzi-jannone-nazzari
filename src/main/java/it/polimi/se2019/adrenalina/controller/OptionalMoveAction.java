package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.model.ActionType;

public class OptionalMoveAction extends MoveAction {
  private final boolean used;
  private final ActionType type;

  public OptionalMoveAction(int target, int destination, boolean used) {
    super(target, destination);
    this.used = used;
    type = ActionType.OPTIONAL_MOVE;
  }

  @Override
  public ActionType getActionType() {
    return type;
  }

  public boolean isUsed() {
    return used;
  }
}
