package it.polimi.se2019.adrenalina.controller.action;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.model.Weapon;

public class ShootAction implements Action {
  private int target;
  private int damages;
  private int tag;
  private ActionType type = ActionType.SHOOT;

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
  public void execute(Weapon weapon) {
    // TODO: shoot
  }

  @Override
  public String serialize(){
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public static ShootAction deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
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

  @Override
  public boolean equals(Object object) {
    return object instanceof Action && ((Action) object).getActionType() == ActionType.SHOOT
        && ((ShootAction) object).target == target
        && ((ShootAction) object).damages == damages
        && ((ShootAction) object).tag == tag;
  }

  @Override
  public int hashCode() {
    return target + damages + tag + type.ordinal();
  }
}
