package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.utils.NotExposeExclusionStrategy;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class defining a weapon.
 */
public class Weapon extends Observable {
  private final AmmoColor baseCost;
  private boolean loaded;
  private final String name;
  private final List<Target> targetHistory;
  private final List<Effect> effects;
  private final List<Effect> selectedEffects;
  private final HashMap<AmmoColor, Integer> cost;

  public Weapon(int costRed, int costBlue, int costYellow,
      AmmoColor baseCost, String name) {
    this.baseCost = baseCost;
    this.name = name;
    loaded = true;

    targetHistory = new ArrayList<>();
    effects = new ArrayList<>();
    selectedEffects = new ArrayList<>();
    cost = new HashMap<>();

    cost.put(AmmoColor.RED, costRed);
    cost.put(AmmoColor.BLUE, costBlue);
    cost.put(AmmoColor.YELLOW, costYellow);
  }

  public Weapon(Weapon weapon) {
    baseCost = weapon.baseCost;
    name = weapon.name;
    loaded = weapon.loaded;

    targetHistory = new ArrayList<>();
    for (Target target : weapon.targetHistory) {
      if (target.isPlayer()) {
        targetHistory.add(new Player((Player) target, true));
      } else {
        targetHistory.add(new Square((Square) target));
      }
    }

    effects = new ArrayList<>();
    for (Effect effect : weapon.effects) {
      effects.add(new Effect(effect));
    }

    selectedEffects = new ArrayList<>();
    for (Effect effect : weapon.selectedEffects) {
      selectedEffects.add(new Effect(effect));
    }

    cost = new HashMap<>();

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

  /**
   * Clear data of previous targets.
   */
  public void clearTargetHistory() {
    targetHistory.clear();
  }

  public void updateTargetHistory(Target target) {
    targetHistory.add(target);
  }

  public List<Target> getTargetHistory() {
    return new ArrayList<>(targetHistory);
  }

  public List<Effect> getEffects() {
    return new ArrayList<>(effects);
  }

  public void addEffect(Effect effect) {
    effects.add(effect);
  }

  public void setSelectedEffect(Effect effect) {
    if (! effects.contains(effect)) {
      throw new IllegalArgumentException("This weapon does not have that effect");
    }
    selectedEffects.add(effect);
  }

  public List<Effect> getSelectedEffects() {
    return new ArrayList<>(selectedEffects);
  }

  /**
   * Clear data of previously selected effects.
   */
  public void clearSelectedEffects() {
    selectedEffects.clear();
  }

  public int getCost(AmmoColor color) {
    return cost.get(color);
  }

  public String serialize() {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.addSerializationExclusionStrategy(new NotExposeExclusionStrategy()).create();
    return gson.toJson(this);
  }

  /**
   * Create Weapon object from json formatted String
   * @param json json input String
   * @return Weapon
   * @exception IllegalArgumentException thrown if argument json is null
   */
  public static Weapon deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    Weapon weapon = gson.fromJson(json, Weapon.class);
    for (Effect effect: weapon.effects) {
      effect.reconcileDeserialization(weapon, null);
    }
    return weapon;
  }
}
