package it.polimi.se2019.adrenalina.controller;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.model.ActionType;

public class OptionalMoveAction implements Action {
  private final int target;
  private final int destination;
  private final boolean used;
  private final ActionType type;

  public OptionalMoveAction(int target, int destination, boolean used) {
    this.target = target;
    this.destination = destination;
    this.used = used;
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
    Gson gson = new Gson();
    return gson.fromJson(json, OptionalMoveAction.class);
  }

  public int getTarget() {
    return target;
  }

  public int getDestination() {
    return destination;
  }

  public boolean isUsed() {
    return used;
  }
}
