package it.polimi.se2019.adrenalina.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Weapon extends Observable {

  private final int costRed;
  private final int costBlue;
  private final int costYellow;
  private final AmmoColor baseCost;
  private boolean loaded;
  private final String name;
  private List<Target> targetHistory;
  private List<Effect> effects;
  private List<Effect> selectedEffects;

  public Weapon(int costRed, int costBlue, int costYellow,
      AmmoColor baseCost, String name) {
    this.costRed = costRed;
    this.costBlue = costBlue;
    this.costYellow = costYellow;
    this.baseCost = baseCost;
    this.name = name;
    loaded = true;
    targetHistory = new ArrayList<>();
    effects = new ArrayList<>();
    selectedEffects = new ArrayList<>();
  }

  public boolean isLoaded() {
    return loaded;
  }

  // TODO: getters and setters for: targetHistory, effects, selectedEffects

  public void setLoaded(boolean loaded) {
    this.loaded = loaded;
  }

  public void clearTargetHistory() {
   targetHistory.clear();
  }

  public void clearSelectedEffects() {
    selectedEffects.clear();
  }

  // TODO: observer methods

}
