package it.polimi.se2019.adrenalina.controller;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.model.ActionType;

public class OptionalMoveAction implements Action {
  private final int target;
  private final int destination;
  private final ActionType type;

  public OptionalMoveAction(int target, int destination) {
    this.target = target;
    this.destination = destination;
    type = ActionType.OPTIONAL_MOVE;
  }

  @Override
  public ActionType getActionType() {
    return type;
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

  public int getTarget() {
    return target;
  }

  public int getDestination() {
    return destination;
  }

  @Override
  public boolean equals(Action action) {
    return action.getActionType() == ActionType.OPTIONAL_MOVE
        && ((OptionalMoveAction) action).target == target
        && ((OptionalMoveAction) action).destination == destination;
  }
}
