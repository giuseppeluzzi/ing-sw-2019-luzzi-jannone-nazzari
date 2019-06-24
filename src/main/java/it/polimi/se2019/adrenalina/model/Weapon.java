package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.event.modelview.EnemyWeaponUpdate;
import it.polimi.se2019.adrenalina.event.modelview.OwnWeaponUpdate;
import it.polimi.se2019.adrenalina.utils.JsonEffectDeserializer;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.NotExpose;
import it.polimi.se2019.adrenalina.utils.NotExposeExclusionStrategy;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class defining a weapon.
 */
public class Weapon extends ExecutableObject implements Buyable {

  private static final long serialVersionUID = -5264181345540286103L;
  private final AmmoColor baseCost;
  private boolean loaded;
  private final String name;
  private final HashMap<AmmoColor, Integer> cost;
  @NotExpose
  private List<Effect> effects = new ArrayList<>();

  // Usage information
  @NotExpose
  private final List<Effect> selectedEffects = new ArrayList<>();

  private final String symbol;

  public Weapon(int costRed, int costBlue, int costYellow,
      AmmoColor baseCost, String name, String symbol, boolean loaded) {
    this(costRed, costBlue, costYellow, baseCost, name, symbol);
    this.loaded = loaded;

  }

  public Weapon(int costRed, int costBlue, int costYellow,
      AmmoColor baseCost, String name, String symbol) {
    this.baseCost = baseCost;
    this.name = name;
    loaded = true;
    this.symbol = symbol;

    cost = new HashMap<>();

    cost.put(AmmoColor.RED, costRed);
    cost.put(AmmoColor.BLUE, costBlue);
    cost.put(AmmoColor.YELLOW, costYellow);
    cost.put(AmmoColor.ANY, 0);
  }

  /**
   * Copy constructor, creates an exact clone of the weapon.
   * @param weapon the weapon to be copied.
   */
  public Weapon(Weapon weapon) {
    baseCost = weapon.baseCost;
    name = weapon.name;
    loaded = weapon.loaded;
    cost = new HashMap<>();
    symbol = weapon.symbol;

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
    try {
      Player owner = getOwner();
      if (owner.getClient() != null) {
        notifyLoaded(owner);
      }
    } catch (IllegalStateException ignore) {
      //
    }
  }

  /**
   * Notifies observers about the loaded status of a weapon.
   * @param owner the player who owns the weapon
   */
  private void notifyLoaded(Player owner) {
    try {
      notifyObservers(new OwnWeaponUpdate(owner.getColor(), owner.getWeapons()));
      notifyObservers(new EnemyWeaponUpdate(owner.getColor(),
              owner.getWeaponCount(), owner.getUnloadedWeapons()));
    } catch (RemoteException e) {
      Log.exception(e);
    }
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
   * Returns list of Effects contained in this weapon.
   * @return list of Effects
   */
  public List<Effect> getEffects() {
    return new ArrayList<>(effects);
  }

  /**
   * Returns an effect based on its name.
   * @param findName the name of the effect
   * @return the effect object
   */
  public Effect getEffectByName(String findName) {
    for (Effect effect : effects) {
      if (effect.getName().equals(findName)) {
        return effect;
      } else {
        Effect subEffect = effect.getSubEffectByName(findName);
        if (subEffect != null) {
          return subEffect;
        }
      }
    }
    return null;
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
    if (!effects.contains(effect)) {
      throw new IllegalArgumentException("This weapon does not have that effect");
    }
    selectedEffects.add(effect);
  }

  /**
   * Returns the owner of the weapon by checking the targetHistory.
   * @return the owner of the weapon
   */
  @Override
  public Player getOwner() {
    if (getTargetHistory(0) == null) {
      throw new IllegalStateException("Target 0 is missing");
    }
    if (!getTargetHistory(0).isPlayer()) {
      throw new IllegalStateException("Target 0 is not a player");
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

  @Override
  public BuyableType getBuyableType() {
    return BuyableType.WEAPON;
  }

  /**
   * Returns how many ammo of specified AmmoColor must be paid in order to reload the weapon.
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

  @Override
  public Buyable getBaseBuyable() {
    return this;
  }

  /**
   * Returns an hashmap where each entry specifies how much of the given color must be paid in order
   * to reload or buy the weapon.
   * @param isReload indicates if the cost is for a reloading action or not
   * @return hashmap of colors and values
   */
  public Map<AmmoColor, Integer> getCost(boolean isReload) {
    HashMap<AmmoColor, Integer> weaponCost = new HashMap<>();
    for (AmmoColor ammoColor : AmmoColor.getValidColor()) {
      weaponCost.put(ammoColor, getCost(ammoColor));
    }
    weaponCost.put(AmmoColor.ANY, 0);
    if (isReload) {
        weaponCost.put(baseCost, weaponCost.get(baseCost) + 1);
    }
    return weaponCost;
  }

  @Override
  public void afterPaymentCompleted(TurnController turnController, Board board, Player player) {
    throw new IllegalStateException("Must use a decorator");
  }

  @Override
  public String getSymbol() {
    return symbol;
  }

  @Override
  public boolean isWeapon() {
    return true;
  }

  /**
   * Gson serialization.
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
    weapon.reset();

    if (weapon.effects != null) {
      for (Effect effect : weapon.effects) {
        effect.reconcileDeserialization(weapon, null);
      }
    }
    return weapon;
  }
}
