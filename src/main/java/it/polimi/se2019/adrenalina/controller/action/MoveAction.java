package it.polimi.se2019.adrenalina.controller.action;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;

public class MoveAction implements Action {

  private static final long serialVersionUID = -2545135795207731324L;
  private int target;
  private int destination;
  private ActionType type = ActionType.MOVE;

  public MoveAction(int target, int destination) {
    this.target = target;
    this.destination = destination;
    type = ActionType.MOVE;
  }

  @Override
  public ActionType getActionType() {
    return type;
  }

  @Override
  public void execute(Board board, Weapon weapon) {

    if (weapon.getTargetHistory(target) != null) {
      try {
        weapon.getTargetHistory(target).getPlayer()
            .setSquare(weapon.getTargetHistory(destination).getSquare());
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
    return object instanceof Action && ((Action) object).getActionType() == ActionType.MOVE
        && ((MoveAction) object).target == target
        && ((MoveAction) object).destination == destination;
  }

  @Override
  public int hashCode() {
    return target + destination + type.ordinal();
  }
}
