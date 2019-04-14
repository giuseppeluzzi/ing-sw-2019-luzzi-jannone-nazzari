package it.polimi.se2019.adrenalina.controller;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.model.ActionType;

public class ShootAction implements Action {
  private final int target;
  private final int damages;
  private final int tag;
  private final ActionType type;

  public ShootAction(int target, int damages, int tag) {
    this.target = target;
    this.damages = damages;
    this.tag = tag;
    type = ActionType.SHOOT;
  }

  @Override
  public ActionType getActionType() {
    return type;
  }

  @Override
  public String serialize(){
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public static ShootAction deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, ShootAction.class);
  }

  public int getTarget() {
    return target;
  }

  public int getDamages() {
    return damages;
  }

  public int getTag() {
    return tag;
  }
}
