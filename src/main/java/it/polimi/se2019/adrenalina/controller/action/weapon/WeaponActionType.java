package it.polimi.se2019.adrenalina.controller.action.weapon;

/**
 * Enumeration of all different types of weapon actions.
 */
public enum WeaponActionType {
  SELECT(SelectAction.class),
  SELECT_DIRECTION(SelectDirectionAction.class),
  OPTIONAL_MOVE(OptionalMoveAction.class),
  MOVE(MoveAction.class),
  SHOOT(ShootAction.class),
  SHOOT_ROOM(ShootRoomAction.class),
  SHOOT_SQUARE(ShootSquareAction.class),
  COPY_SQUARE(CopySquareAction.class);

  private final Class<? extends WeaponAction> actionClass;

  WeaponActionType(Class<? extends WeaponAction> actionClass) {
    this.actionClass = actionClass;
  }

  public Class<? extends WeaponAction> getActionClass() {
    return actionClass;
  }
}

