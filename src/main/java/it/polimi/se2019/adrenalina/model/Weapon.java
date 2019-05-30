package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.utils.JsonEffectDeserializer;
import it.polimi.se2019.adrenalina.utils.NotExpose;
import it.polimi.se2019.adrenalina.utils.NotExposeExclusionStrategy;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class defining a weapon.
 */
public class Weapon extends Observable implements ExecutableObject, Buyable {

  private static final long serialVersionUID = -5264181345540286103L;
  private final AmmoColor baseCost;
  @NotExpose
  private boolean loaded;
  private final String name;
  private final HashMap<AmmoColor, Integer> cost;
  private Integer currentSelectTargetSlot;
  @NotExpose
  private HashMap<Integer, Boolean> optMoveGroups = new HashMap<>();
  private List<Effect> effects = new ArrayList<>();

  // Usage information
  @NotExpose
  private HashMap<Integer, Target> targetHistory = new HashMap<>();
  @NotExpose
  private final List<Effect> selectedEffects = new ArrayList<>();
  private boolean didShoot;
  private Direction lastUsageDirection;
  private boolean cancelled;
  @NotExpose
  private HashMap<Player, Square> initialPlayerPositions = new HashMap<>();
  // TODO: attribute to count if the optionalmoveaction is used

  public Weapon(int costRed, int costBlue, int costYellow,
      AmmoColor baseCost, String name) {
    this.baseCost = baseCost;
    this.name = name;
    loaded = true;
    didShoot = false;

    cost = new HashMap<>();

    cost.put(AmmoColor.RED, costRed);
    cost.put(AmmoColor.BLUE, costBlue);
    cost.put(AmmoColor.YELLOW, costYellow);
    cost.put(AmmoColor.ANY, 0);
  }

  public Weapon(Weapon weapon) {
    baseCost = weapon.baseCost;
    name = weapon.name;
    loaded = weapon.loaded;
    cost = new HashMap<>();

    for (Map.Entry<Integer, Target> entry : weapon.targetHistory.entrySet()) {
      Integer key = entry.getKey();
      Target value = entry.getValue();
      if (value.isPlayer()) {
        targetHistory.put(key, new Player((Player) value, true));
      } else {
        targetHistory.put(key, new Square((Square) value));
      }
    }

    didShoot = weapon.didShoot;
    initialPlayerPositions = new HashMap<>(weapon.initialPlayerPositions);

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
    cost.put(AmmoColor.ANY, 0);
  }

  /**
   * Returns whether the weapon is loaded or not.
   *
   * @return true if weapon is loaded, false otherwise
   */
  public boolean isLoaded() {
    return loaded;
  }

  /**
   * Set if weapon is loaded or not.
   *
   * @param loaded, true if weapon is loaded, false otherwise
   */
  public void setLoaded(boolean loaded) {
    this.loaded = loaded;
  }

  /**
   * Returns the color of the weapon's base cost.
   *
   * @return baseCost of weapon
   */
  public AmmoColor getBaseCost() {
    return baseCost;
  }

  /**
   * Returns the weapon's name.
   *
   * @return the weapon's name
   */
  public String getName() {
    return name;
  }

  @Override
  public Target getTargetHistory(Integer key) {
    return targetHistory.get(key);
  }

  @Override
  public void setTargetHistory(Integer key, Target value) {
    targetHistory.put(key, value);
  }

  /**
   * Returns whether any {@code OptionalMoveAction} from group of index "key" has been executed.
   *
   * @param key group id
   * @return true if group id "key" has been previously executed, false otherwise
   */
  public Boolean isGroupMoveUsed(Integer key) {
    return optMoveGroups.get(key);
  }

  /**
   * Whenever an optional move weaponaction is executed an entry with values "true, group_id" is
   * created and no more move actions of that group can be executed.
   *
   * @param key group id of executed move weaponaction
   */
  public void setGroupMoveUsed(Integer key) {
    optMoveGroups.put(key, true);
  }

  /**
   * Returns list of Effects contained in this weapon.
   *
   * @return list of Effects
   */
  public List<Effect> getEffects() {
    return new ArrayList<>(effects);
  }

  /**
   * Returns an effect based on its name.
   *
   * @param findName the name of the effect
   * @return the effect object
   */
  public Effect getEffectByName(String findName) {
    for (Effect effect : effects) {
      if (effect.getName().equals(findName)) {
        return effect;
      }
    }
    return null;
  }

  /**
   * Add an Effect to the weapon's list.
   *
   * @param effect Effect to be added
   */
  public void addEffect(Effect effect) {
    effects.add(effect);
  }

  /**
   * Select an Effect and places it in selectedEffects list.
   *
   * @param effect Effect to be placed in selectedEffets
   */
  public void setSelectedEffect(Effect effect) {
    if (!effects.contains(effect)) {
      throw new IllegalArgumentException("This weapon does not have that effect");
    }
    selectedEffects.add(effect);
  }

  @Override
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
   *
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

  @Override
  public BuyableType getBuyableType() {
    return BuyableType.WEAPON;
  }

  /**
   * Returns how many ammo of specified AmmoColor must be paid in order to reload the weapon.
   *
   * @param color color of the ammo
   * @return how many ammo of specified AmmoColor must be paid
   */
  @Override
  public int getCost(AmmoColor color) {
    if (color == AmmoColor.ANY) {
      return 0;
    }
    return cost.get(color);
  }

  /**
   * Return last direction used from an effect of the weapon.
   *
   * @return lastUsageDirection
   */
  public Direction getLastUsageDirection() {
    return lastUsageDirection;
  }

  /**
   * Set last direction used.
   *
   * @param lastUsageDirection last direction used
   */
  public void setLastUsageDirection(Direction lastUsageDirection) {
    this.lastUsageDirection = lastUsageDirection;
  }

  /**
   * Sets currentSelectTargetSlot value.
   * @param slot
   */
  @Override
  public void setCurrentSelectTargetSlot(Integer slot) {
    currentSelectTargetSlot = slot;
  }

  /**
   * Return currentSelectTargetSlot value.
   *
   * @return currentSelectTargetSlot
   */
  @Override
  public Integer getCurrentSelectTargetSlot() {
    return currentSelectTargetSlot;
  }

  /**
   * Gson serialization.
   *
   * @return JSON string containing serialized object
   */
  public String serialize() {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.addSerializationExclusionStrategy(new NotExposeExclusionStrategy())
        .create();
    return gson.toJson(this);
  }

  /**
   * Create Weapon object from json formatted String
   *
   * @param json json input String
   * @return Weapon
   * @throws IllegalArgumentException thrown if argument json is null
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

    weapon.setObservers(new ArrayList<>());
    weapon.optMoveGroups = new HashMap<>();
    weapon.targetHistory = new HashMap<>();
    weapon.initialPlayerPositions = new HashMap<>();

    for (Effect effect : weapon.effects) {
      effect.reconcileDeserialization(weapon, null);
    }

    return weapon;
  }

  @Override
  public void afterPaymentCompleted(TurnController turnController, Board board, Player player) {
    if (player.hasWeapon(this)) {
      setLoaded(true);
    } else {
      for (Square square : board.getSquares()) {
        if (square.getWeapons().contains(this)) {
          square.removeWeapon(this);
          break;
        }
      }

      player.addWeapon(this);
    }
  }

  @Override
  public void setDidShoot() {
    didShoot = true;
  }

  @Override
  public boolean didShoot() {
    return didShoot;
  }

  @Override
  public Map<Player, Square> getInitialPlayerPositions() {
    return new HashMap<>(initialPlayerPositions);
  }

  @Override
  public void setInitialPlayerPosition(Player player, Square position) {
    initialPlayerPositions.put(player, position);
  }

  @Override
  public boolean isInitialPositionSet(Player player) {
    return initialPlayerPositions.containsKey(player);
  }

  public void setCancelled() {
    cancelled = true;
  }

  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void reset() {
    Target currentOwner = targetHistory.get(0);
    targetHistory.clear();
    setTargetHistory(0, currentOwner);
    setCurrentSelectTargetSlot(null);
    didShoot = false;
    initialPlayerPositions = new HashMap<>();
    cancelled = false;
  }
}
