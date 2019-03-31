package it.polimi.se2019.adrenalina.model;

import com.sun.tools.javac.util.List;

public class SelectAction implements Action {
  private int from;
  private int target;
  private int minDistance;
  private int maxDistance;
  private int differentFrom;
  private boolean visible;
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

  public SelectAction setMinDistance(int value) {
    minDistance = value;
    return this;
  }

  public SelectAction setMaxDistance(int value) {
    maxDistance = value;
    return this;
  }

  public SelectAction setDifferentFrom(int target) {
    differentFrom = target;
    return this;
  }

  public SelectAction setVisibility(boolean value) {
    visible = value;
    return this;
  }

  public List<Target> filter(List<Target> targets) {
    return null;
    // TODO implement filter
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
