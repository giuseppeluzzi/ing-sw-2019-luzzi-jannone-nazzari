package it.polimi.se2019.adrenalina.controller;

import com.google.gson.Gson;

public class ShootRoomAction extends ShootAction {
  private final ActionType type;

  public ShootRoomAction(int target, int damages, int tag) {
    super(target, damages, tag);
    type = ActionType.SHOOT_ROOM;
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

  public static ShootRoomAction deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, ShootRoomAction.class);
  }


  @Override
  public boolean equals(Object object) {
    return object instanceof Action && ((Action) object).getActionType() == ActionType.SHOOT_ROOM
        && ((ShootAction) object).getTarget() == getTarget()
        && ((ShootAction) object).getDamages() == getDamages()
        && ((ShootAction) object).getTag() == getTag();
  }

  @Override
  public int hashCode() {
    return getTarget() * getDamages() * getTag() * type.ordinal();
  }
}
