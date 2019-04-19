package it.polimi.se2019.adrenalina.controller;

import com.google.gson.Gson;

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
    return target * destination * type.ordinal();
  }
}
