package it.polimi.se2019.adrenalina.controller.action.weapon;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.ExecutableObject;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;

/**
 * Action used to move a player on the board; only executed if no other actions of the same type and with
 * the same group number were previously executed and if a valid target has been selected.
 */
public class OptionalMoveAction implements WeaponAction {

  private static final long serialVersionUID = -6483684905618211881L;
  private int target;
  private int destination;
  private int group;
  private WeaponActionType type = WeaponActionType.OPTIONAL_MOVE;

  public OptionalMoveAction(int target, int destination, int group) {
    this.target = target;
    this.destination = destination;
    this.group = group;
    type = WeaponActionType.OPTIONAL_MOVE;
  }

  @Override
  public WeaponActionType getActionType() {
    return type;
  }

  @Override
  public void execute(Board board, ExecutableObject object) {
    Weapon weapon = (Weapon) object;
    if (!weapon.isGroupMoveUsed(group) && weapon.getTargetHistory(target) != null) {
      try {
        weapon.getTargetHistory(target).getPlayer()
            .setSquare(weapon.getTargetHistory(destination).getSquare());
        weapon.setGroupMoveUsed(group);
      } catch (InvalidSquareException e) {
        Log.debug("Too many players in selected square");
      }
    }
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public static OptionalMoveAction deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, OptionalMoveAction.class);
  }

  public int getGroup() {
    return group;
  }

  public int getTarget() {
    return target;
  }

  public int getDestination() {
    return destination;
  }

  @Override
  public boolean equals(Object object) {
    return object instanceof WeaponAction && ((WeaponAction) object).getActionType() == WeaponActionType.OPTIONAL_MOVE
        && ((OptionalMoveAction) object).target == target
        && ((OptionalMoveAction) object).destination == destination;
  }

  @Override
  public int hashCode() {
    return target + destination + type.ordinal();
  }
}
