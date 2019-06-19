package it.polimi.se2019.adrenalina.controller.action.weapon;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.ExecutableObject;
import it.polimi.se2019.adrenalina.utils.Log;

/**
 * Action used to move a player on the board.
 */
public class MoveAction implements WeaponAction {

  private static final long serialVersionUID = -2545135795207731324L;
  private int target;
  private int destination;
  private WeaponActionType type = WeaponActionType.MOVE;

  public MoveAction(int target, int destination) {
    this.target = target;
    this.destination = destination;
    type = WeaponActionType.MOVE;
  }

  @Override
  public WeaponActionType getActionType() {
    return type;
  }

  @Override
  public void execute(Board board, ExecutableObject object) {
    if (object.getTargetHistory(target) != null) {
      try {
        if (! object.isInitialPositionSet(object.getTargetHistory(target).getPlayer())) {
          object.setInitialPlayerPosition(
              object.getTargetHistory(target).getPlayer(),
              object.getTargetHistory(target).getPlayer().getSquare());
        }
        object.getTargetHistory(target).getPlayer()
            .setSquare(object.getTargetHistory(destination).getSquare());
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

  public static MoveAction deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, MoveAction.class);
  }

  public int getTarget() {
    return target;
  }

  public int getDestination() {
    return destination;
  }

  @Override
  public boolean equals(Object object) {
    return object instanceof WeaponAction && ((WeaponAction) object).getActionType() == WeaponActionType.MOVE
        && ((MoveAction) object).target == target
        && ((MoveAction) object).destination == destination;
  }

  @Override
  public int hashCode() {
    return target + destination + type.ordinal();
  }
}
