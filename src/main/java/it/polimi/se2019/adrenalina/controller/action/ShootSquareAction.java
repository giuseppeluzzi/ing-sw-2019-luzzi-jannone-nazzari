package it.polimi.se2019.adrenalina.controller.action;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Weapon;

public class ShootSquareAction extends ShootAction  {
  private final int distance;
  private final int[] exclude;

  public ShootSquareAction(int target, int damages, int tag, int distance, int[] exclude) {
    super(target, damages, tag);
    this.distance = distance;
    this.exclude = exclude.clone();
    type = ActionType.SHOOT_SQUARE;
  }


  @Override
  public void execute(Board board, Weapon weapon) {
    // TODO: shoot every player in the square
  }

  @Override
  public String serialize(){
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public int getDistance() {
    return distance;
  }

  public static ShootSquareAction deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, ShootSquareAction.class);
  }

  public int[] getExclude() {
    return exclude.clone();
  }

  @Override
  public boolean equals(Object object) {
    return object instanceof Action && ((Action) object).getActionType() == ActionType.SHOOT_SQUARE
        && ((ShootAction) object).getTarget() == getTarget()
        && ((ShootAction) object).getDamages() == getDamages()
        && ((ShootSquareAction) object).getExclude() == getExclude()
        && ((ShootAction) object).getTag() == getTag();
  }

  @Override
  public int hashCode() {
    return getTarget() + getDamages() + getTag() + type.ordinal() + getExclude().length;
  }
}
