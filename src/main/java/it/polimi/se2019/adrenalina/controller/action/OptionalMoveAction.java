package it.polimi.se2019.adrenalina.controller.action;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.model.Weapon;

public class OptionalMoveAction implements Action {
  private int target;
  private int destination;
  private int group;
  private ActionType type = ActionType.OPTIONAL_MOVE;

  public OptionalMoveAction(int target, int destination, int group) {
    this.target = target;
    this.destination = destination;
    this.group = group;
    type = ActionType.OPTIONAL_MOVE;
  }

  @Override
  public ActionType getActionType() {
    return type;
  }

  @Override
  public void execute(Weapon weapon) {
    // TODO: moves a player if the movement isn't used yet (see weapon.optMoveGroups)
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
    return object instanceof Action && ((Action) object).getActionType() == ActionType.OPTIONAL_MOVE
        && ((OptionalMoveAction) object).target == target
        && ((OptionalMoveAction) object).destination == destination;
  }

  @Override
  public int hashCode() {
    return target + destination + type.ordinal();
  }
}
