package it.polimi.se2019.adrenalina.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Observable;

public class Weapon extends Observable {
  private final AmmoColor baseCost;
  private boolean loaded;
  private final String name;
  private final List<Target> targetHistory;
  private final List<Effect> effects;
  private final List<Effect> selectedEffects;
  private final EnumMap<AmmoColor, Integer> cost;

  public Weapon(int costRed, int costBlue, int costYellow,
      AmmoColor baseCost, String name) {
    this.baseCost = baseCost;
    this.name = name;
    loaded = true;

    targetHistory = new ArrayList<>();
    effects = new ArrayList<>();
    selectedEffects = new ArrayList<>();
    cost = new EnumMap<>(AmmoColor.class);

    cost.put(AmmoColor.RED, costRed);
    cost.put(AmmoColor.BLUE, costBlue);
    cost.put(AmmoColor.YELLOW, costYellow);
  }

  public boolean isLoaded() {
    return loaded;
  }

  public void setLoaded(boolean loaded) {
    this.loaded = loaded;
  }

  public AmmoColor getBaseCost() {
    return baseCost;
  }

  public String getName() {
    return name;
  }

  public void clearTargetHistory() {
    targetHistory.clear();
  }

  public List<Target> getTargetHistory() {
    // TODO: targetHistory is mutable
    return new ArrayList<>();
  }

  public List<Effect> getEffects() {
    // TODO: effects is mutable
    return new ArrayList<>();
  }

  public void addEffect(Effect effect) {
    effects.add(effect);
  }

  public List<Effect> getSelectedEffects() {
    // TODO: selectedEffects is mutable
    return new ArrayList<>();
  }

  public void clearSelectedEffects() {
    selectedEffects.clear();
  }

  public int getCost(AmmoColor color) {
    return cost.get(color);
  }
}
