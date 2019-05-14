package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.utils.JsonEffectDeserializer;
import it.polimi.se2019.adrenalina.utils.NotExpose;
import it.polimi.se2019.adrenalina.utils.NotExposeExclusionStrategy;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class defining a weapon.
 */
public class Weapon extends Observable implements Serializable {

  private static final long serialVersionUID = -5264181345540286103L;
  private final AmmoColor baseCost;
  @NotExpose
  private boolean loaded;
  private final String name;
  private final HashMap<AmmoColor, Integer> cost = new HashMap<>();
  @NotExpose
  private final HashMap<Integer, Boolean> optMoveGroups = new HashMap<>();
  private final List<Effect> effects = new ArrayList<>();

  // Usage information
  @NotExpose
  private final HashMap<Integer, Target> targetHistory = new HashMap<>();
  @NotExpose
  private final List<Effect> selectedEffects = new ArrayList<>();
  private Direction lastUsageDirection = null;
  // TODO: attribute to count if the optionalmoveaction is used

  public Weapon(int costRed, int costBlue, int costYellow,
      AmmoColor baseCost, String name) {
    this.baseCost = baseCost;
    this.name = name;
    loaded = true;

    cost.put(AmmoColor.RED, costRed);
    cost.put(AmmoColor.BLUE, costBlue);
    cost.put(AmmoColor.YELLOW, costYellow);
  }

  public Weapon(Weapon weapon) {
    baseCost = weapon.baseCost;
    name = weapon.name;
    loaded = weapon.loaded;

    for (Map.Entry<Integer, Target> entry : weapon.targetHistory.entrySet()) {
      Integer key = entry.getKey();
      Target value = entry.getValue();
      if (value.isPlayer()) {
        targetHistory.put(key, new Player((Player) value, true));
      } else {
        targetHistory.put(key, new Square((Square) value));
      }
    }

    optMoveGroups.putAll(weapon.optMoveGroups);

    for (Effect effect : weapon.effects) {
      effects.add(new Effect(effect));
    }

    for (Effect effect : weapon.selectedEffects) {
      selectedEffects.add(new Effect(effect));
    }

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

  public Target getTargetHistory(Integer key) {
    return targetHistory.get(key);
  }

  public void setTargetHistory(Integer key, Target value) {
    targetHistory.put(key, value);
  }

  public Boolean isGroupMoveUsed(Integer key) {
    return optMoveGroups.get(key);
  }

  /**
   * Whenever an optional move action is executed an entry with values "true, group_id" is created
   * and no more move actions of that group can be executed
   * @param key group id of executed move action
   */
  public void setGroupMoveUsed(Integer key) {
    optMoveGroups.put(key, true);
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

  public Player getOwner() {
    if (getTargetHistory(0) == null) {
      throw new IllegalStateException("Target 0 is missing");
    }
    if (!getTargetHistory(0).isPlayer()) {
      throw new IllegalStateException("Target 0 is  not a player");
    }
    return (Player) getTargetHistory(0);
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

  public Direction getLastUsageDirection() {
    return lastUsageDirection;
  }

  public void setLastUsageDirection(Direction lastUsageDirection) {
    this.lastUsageDirection = lastUsageDirection;
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
    GsonBuilder builder = new GsonBuilder();
    JsonDeserializer<Effect> effectJsonDeserializer = new JsonEffectDeserializer();
    builder.registerTypeAdapter(Effect.class, effectJsonDeserializer);

    Gson gson = builder.create();
    Weapon weapon = gson.fromJson(json, Weapon.class);

    for (Effect effect: weapon.effects) {
      effect.reconcileDeserialization(weapon, null);
    }

    return weapon;
  }
}
