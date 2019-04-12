package it.polimi.se2019.adrenalina.controller;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.model.ActionType;

public class MoveAction implements Action{
  private final int target;
  private final int destination;
  protected final ActionType type;

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

  @Override
  public MoveAction deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, MoveAction.class);
  }

  public int getTarget() {
    return target;
  }


  public int getDestination() {
    return destination;
  }
}
