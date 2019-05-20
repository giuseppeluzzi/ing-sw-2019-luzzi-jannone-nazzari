package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.controller.action.Action;
import it.polimi.se2019.adrenalina.controller.action.ActionType;
import it.polimi.se2019.adrenalina.controller.action.SelectAction;
import it.polimi.se2019.adrenalina.utils.JsonEffectDeserializer;
import it.polimi.se2019.adrenalina.utils.NotExpose;
import it.polimi.se2019.adrenalina.utils.NotExposeExclusionStrategy;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
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
  private Deque<Action> actionsQueue = new ArrayDeque<>();
  private Integer currentSelectTargetSlot;

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

  /**
   * Returns whether the weapon is loaded or not.
   * @return true if weapon is loaded, false otherwise
   */
  public boolean isLoaded() {
    return loaded;
  }

  /**
   * Set if weapon is loaded or not.
   * @param loaded, true if weapon is loaded, false otherwise
   */
  public void setLoaded(boolean loaded) {
    this.loaded = loaded;
  }

  /**
   * Returns the color of the weapon's base cost.
   * @return baseCost of weapon
   */
  public AmmoColor getBaseCost() {
    return baseCost;
  }

  /**
   * Returns the weapon's name.
   * @return the weapon's name
   */
  public String getName() {
    return name;
  }

  /**
   * Clear data of previous targets.
   */
  public void clearTargetHistory() {
    targetHistory.clear();
  }

  /**
   * Return Target element from targetHistory at index "key".
   * @param key index of the target requested
   * @return Target element at specified index
   */
  public Target getTargetHistory(Integer key) {
    return targetHistory.get(key);
  }

  /**
   * Insert in targetHistory at index "key" Target "value".
   * @param key index where Target has to be inserted
   * @param value Target to be inserted
   */
  public void setTargetHistory(Integer key, Target value) {
    targetHistory.put(key, value);
  }

  /**
   * Returns whether any {@code OptionalMoveAction} from group of index "key" has been executed.
   * @param key group id
   * @return true if group id "key" has been previously executed, false otherwise
   */
  public Boolean isGroupMoveUsed(Integer key) {
    return optMoveGroups.get(key);
  }

  /**
   * Whenever an optional move action is executed an entry with values "true, group_id" is created
   * and no more move actions of that group can be executed.
   * @param key group id of executed move action
   */
  public void setGroupMoveUsed(Integer key) {
    optMoveGroups.put(key, true);
  }

  /**
   * Returns list of Effects contained in this weapon.
   * @return list of Effects
   */
  public List<Effect> getEffects() {
    return new ArrayList<>(effects);
  }

  /**
   * Add an Effect to the weapon's list.
   * @param effect Effect to be added
   */
  public void addEffect(Effect effect) {
    effects.add(effect);
  }

  /**
   * Select an Effect and places it in selectedEffects list.
   * @param effect Effect to be placed in selectedEffets
   */
  public void setSelectedEffect(Effect effect) {
    if (! effects.contains(effect)) {
      throw new IllegalArgumentException("This weapon does not have that effect");
    }
    selectedEffects.add(effect);
  }

  /**
   * Returns Player owning the weapon, throws an exception otherwise.
   * @return Player if existing
   */
  public Player getOwner() {
    if (getTargetHistory(0) == null) {
      throw new IllegalStateException("Target 0 is missing");
    }
    if (!getTargetHistory(0).isPlayer()) {
      throw new IllegalStateException("Target 0 is  not a player");
    }
    return (Player) getTargetHistory(0);
  }

  /**
   * Returns selectedEffects.
   * @return list of selected effects
   */
  public List<Effect> getSelectedEffects() {
    return new ArrayList<>(selectedEffects);
  }

  /**
   * Clear data of previously selected effects.
   */
  public void clearSelectedEffects() {
    selectedEffects.clear();
  }

  /**
   * Returns how many ammo of specified AmmoColor must be paid in order to reload the weapon.
   * @param color color of the ammo
   * @return how many ammo of specified AmmoColor must be paid
   */
  public int getCost(AmmoColor color) {
    return cost.get(color);
  }

  /**
   * Return last direction used from an effect of the weapon.
   * @return lastUsageDirection
   */
  public Direction getLastUsageDirection() {
    return lastUsageDirection;
  }

  /**
   * Set last direction used.
   * @param lastUsageDirection last direction used
   */
  public void setLastUsageDirection(Direction lastUsageDirection) {
    this.lastUsageDirection = lastUsageDirection;
  }

  /**
   * Return currentSelectTargetSlot value.
   * @return currentSelectTargetSlot
   */
  public Integer getCurrentSelectTargetSlot() {
    return currentSelectTargetSlot;
  }

  /**
   * Add all the actions of effect in the queue
   * @param effect Effect whose actions will be added
   * @throws IllegalArgumentException thrown if effect has no action
   */
  public void enqueue(Effect effect) {
    if (effect.getActions().isEmpty()) {
      throw new IllegalArgumentException("effect does not contain any action");
    }
    for (Action action : effect.getActions()) {
      actionsQueue.addLast(action);
    }
  }

  /**
   * All the actions in the queue are executed with LIFO methodology.
   * @param board Board object, must be passed as the argument of execute method
   */
  public void executeActionQueue(Board board) {
    while ( !actionsQueue.isEmpty()) {
      Action action = actionsQueue.getFirst();
      action.execute(board, this);
      actionsQueue.removeFirst();
      if (action.getActionType() == ActionType.SELECT) {
        currentSelectTargetSlot = ((SelectAction) action).getTarget();
      }
      if (action.getActionType() == ActionType.SELECT_DIRECTION || action.getActionType() == ActionType.SELECT) {
        break;
      }
      currentSelectTargetSlot = null;
    }
    getOwner().setCurrentWeapon(null);
    // TODO: Player has completed its turn?
  }

  /**
   * Returns if actionsQueue is empty
   * @return true if empty, false otherwhise
   */
  public boolean isQueueEmpty() {
    return actionsQueue.isEmpty();
  }

  /**
   * Gson serialization.
   * @return JSON string containing serialized object
   */
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
