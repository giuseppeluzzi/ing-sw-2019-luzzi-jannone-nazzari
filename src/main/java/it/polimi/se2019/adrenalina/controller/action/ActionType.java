package it.polimi.se2019.adrenalina.controller.action;

/**
 * Enumeration of all different types of possible actions
 */
public enum ActionType {
  SELECT(SelectAction.class),
  SELECT_DIRECTION(SelectDirectionAction.class),
  OPTIONAL_MOVE(OptionalMoveAction.class),
  MOVE(MoveAction.class),
  SHOOT(ShootAction.class),
  SHOOT_ROOM(ShootRoomAction.class),
  SHOOT_SQUARE(ShootSquareAction.class);

  private final Class<? extends Action> actionClass;

  ActionType(Class<? extends Action> actionClass) {
    this.actionClass = actionClass;
  }

  public Class<? extends Action> getActionClass() {
    return actionClass;
  }
}

