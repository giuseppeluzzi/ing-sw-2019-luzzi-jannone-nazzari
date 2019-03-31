package it.polimi.se2019.adrenalina.model;

import java.util.ArrayList;
import java.util.List;

public class SelectAction implements Action {
  private final int from;
  private final int target;
  private final int minDistance;
  private final int maxDistance;
  private final int differentFrom;
  private final boolean visible;
  private final ActionType type;

  public SelectAction(int from, int target, int minDistance,
      int maxDistance, int differentFrom, boolean visible) {

    this.from = from;
    this.target = target;
    this.minDistance = minDistance;
    this.maxDistance = maxDistance;
    this.differentFrom = differentFrom;
    this.visible = visible;
    type = ActionType.SELECT;
  }

  @Override
  public ActionType getActionType() {
    return type;
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

  public int getDifferentFrom() {
    return differentFrom;
  }

  public boolean isVisible() {
    return visible;
  }
}
