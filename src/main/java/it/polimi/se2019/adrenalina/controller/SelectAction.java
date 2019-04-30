package it.polimi.se2019.adrenalina.controller;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.model.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectAction implements Action {

  private final int from;
  private final int target;
  private final int minDistance;
  private final int maxDistance;
  private final int[] differentFrom;
  private final boolean visible;
  private final boolean optional;
  private final ActionType type;

  public SelectAction(int from, int target, int minDistance,
      int maxDistance, int[] differentFrom, boolean visible, boolean optional) {

    this.from = from;
    this.target = target;
    this.minDistance = minDistance;
    this.maxDistance = maxDistance;
    this.differentFrom = differentFrom.clone();
    this.visible = visible;
    this.optional = optional;
    type = ActionType.SELECT;
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

  public static SelectAction deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, SelectAction.class);
  }

  public List<Target> filter(List<Target> targets) {
    // TODO: implement filter
    return new ArrayList<>();
  }

  public int getFrom() {
    return from;
  }

  public int getTarget() {
    return target;
  }

  public int getMinDistance() {
    return minDistance;
  }

  public int getMaxDistance() {
    return maxDistance;
  }

  public int[] getDifferentFrom() {
    return differentFrom.clone();
  }

  public boolean isVisible() {
    return visible;
  }

  public boolean isOptional() {
    return optional;
  }

  @Override
  public boolean equals(Object object) {
    return object instanceof Action && ((Action) object).getActionType() == ActionType.SELECT
        && ((SelectAction) object).visible == visible
        && Arrays.equals(((SelectAction) object).differentFrom, differentFrom)
        && ((SelectAction) object).from == from
        && ((SelectAction) object).minDistance == minDistance
        && ((SelectAction) object).maxDistance == maxDistance
        && ((SelectAction) object).target == target;
  }

  @Override
  public int hashCode() {
    return differentFrom.length * Arrays.hashCode(differentFrom) * from * minDistance * maxDistance * target * type.ordinal();
  }
}
