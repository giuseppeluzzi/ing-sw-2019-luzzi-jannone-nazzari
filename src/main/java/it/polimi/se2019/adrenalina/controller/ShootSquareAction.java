package it.polimi.se2019.adrenalina.controller;

import com.google.gson.Gson;

public class ShootSquareAction extends ShootAction  {
  private final ActionType type;

  public ShootSquareAction(int target, int damages, int tag) {
    super(target, damages, tag);
    type = ActionType.SHOOT_SQUARE;
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

  public static ShootSquareAction deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, ShootSquareAction.class);
  }

  @Override
  public boolean equals(Object object) {
    return object instanceof Action && ((Action) object).getActionType() == ActionType.SHOOT_SQUARE
        && ((ShootAction) object).getTarget() == getTarget()
        && ((ShootAction) object).getDamages() == getDamages()
        && ((ShootAction) object).getTag() == getTag();
  }

  @Override
  public int hashCode() {
    return getTarget() * getDamages() * getTag() * type.ordinal();
  }
}
