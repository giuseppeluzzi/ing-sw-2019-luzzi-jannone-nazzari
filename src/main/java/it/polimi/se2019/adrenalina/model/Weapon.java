package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

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

  public Weapon(Weapon weapon) {
    baseCost = weapon.baseCost;
    name = weapon.name;
    loaded = weapon.loaded;

    targetHistory = weapon.getTargetHistory();
    effects = weapon.getEffects();
    selectedEffects = weapon.getSelectedEffects();
    cost = new EnumMap<>(AmmoColor.class);

    cost.put(AmmoColor.RED, weapon.getCost(AmmoColor.RED));
    cost.put(AmmoColor.BLUE, weapon.getCost(AmmoColor.BLUE));
    cost.put(AmmoColor.YELLOW, weapon.getCost(AmmoColor.YELLOW));
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
    List<Target> output = new ArrayList<>();
    for (Target target : targetHistory) {
      if (target.isPlayer()) {
        output.add(new Player((Player) target, true));
      } else {
        output.add(new Square((Square) target));
      }
    }
    return output;
  }

  public List<Effect> getEffects() {
    List<Effect> output = new ArrayList<>();
    for (Effect effect : effects) {
      output.add(new Effect(effect));
    }
    return output;
  }

  public void addEffect(Effect effect) {
    effects.add(effect);
  }

  public List<Effect> getSelectedEffects() {
    List<Effect> output = new ArrayList<>();
    for (Effect effect : selectedEffects) {
      output.add(new Effect(effect));
    }
    return output;
  }

  public void clearSelectedEffects() {
    selectedEffects.clear();
  }

  public int getCost(AmmoColor color) {
    return cost.get(color);
  }
}
