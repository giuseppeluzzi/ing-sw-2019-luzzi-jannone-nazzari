package it.polimi.se2019.adrenalina.controller.action.weapon;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.ExecutableObject;

public class ShootAction implements WeaponAction {

  private static final long serialVersionUID = 3519195918261269154L;
  private int target;
  private int damages;
  private int tag;
  WeaponActionType type = WeaponActionType.SHOOT;

  public ShootAction(int target, int damages, int tag) {
    this.target = target;
    this.damages = damages;
    this.tag = tag;
    type = WeaponActionType.SHOOT;
  }

  @Override
  public WeaponActionType getActionType() {
    return type;
  }

  @Override
  public void execute(Board board, ExecutableObject object) {
    if (object.targetHistoryContainsKey(target)) {
      object.getTargetHistory(target).addDamages(object.getOwner().getColor(), damages);
      object.getTargetHistory(target).addTags(object.getOwner().getColor(), tag);
    }
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
    return object instanceof WeaponAction && ((WeaponAction) object).getActionType() == WeaponActionType.SHOOT
        && ((ShootAction) object).target == target
        && ((ShootAction) object).damages == damages
        && ((ShootAction) object).tag == tag;
  }

  @Override
  public int hashCode() {
    return target + damages + tag + type.ordinal();
  }
}
