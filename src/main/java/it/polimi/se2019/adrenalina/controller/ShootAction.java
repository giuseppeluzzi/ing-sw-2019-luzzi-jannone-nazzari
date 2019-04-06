package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.model.ActionType;

public class ShootAction implements Action {
  private final int target;
  private final int damages;
  private final int tag;
  private final ActionType type;

  public ShootAction(int target, int damages, int tag) {
    this.target = target;
    this.damages = damages;
    this.tag = tag;
    type = ActionType.SHOOT;
  }

  @Override
  public ActionType getActionType() {
    return type;
  }

  public int getTarget() {
    return target;
  }

  public int getDamages() {
    return damages;
  }

  public int getTag() {
    return tag;
  }
}
