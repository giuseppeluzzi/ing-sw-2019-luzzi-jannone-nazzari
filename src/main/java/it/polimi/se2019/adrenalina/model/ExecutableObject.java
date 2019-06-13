package it.polimi.se2019.adrenalina.model;


import it.polimi.se2019.adrenalina.utils.NotExpose;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.util.HashMap;
import java.util.Map;

public abstract class ExecutableObject extends Observable {

  private boolean didShoot;
  private Direction lastUsageDirection;
  private Integer currentSelectTargetSlot;
  @NotExpose
  private HashMap<Integer, Target> targetHistory = new HashMap<>();
  @NotExpose
  private HashMap<Player, Square> initialPlayerPositions = new HashMap<>();
  private boolean cancelled;

  protected ExecutableObject() {
    didShoot = false;
    targetHistory = new HashMap<>();
    initialPlayerPositions = new HashMap<>();
  }

  /**
   * Clear data of previous targets.
   */
  public void reset() {
    if (targetHistory != null && targetHistory.get(0) != null) {
      Target owner = targetHistory.get(0);
      targetHistory.clear();
      targetHistory.put(0, owner);
    } else {
      targetHistory = new HashMap<>();
    }
    didShoot = false;
    initialPlayerPositions = new HashMap<>();
    currentSelectTargetSlot = null;
    lastUsageDirection = null;
    cancelled = false;
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
   * Returns Player owning the object, throws an exception otherwise.
   * @return Player if existing
   */
  public Player getOwner() {

    if (targetHistory == null || targetHistory.get(0) == null) {
        throw new IllegalStateException("Target 0 is missing in PowerUp");
      }
    return (Player) targetHistory.get(0);
  }

  public void setDidShoot() {
    didShoot = false;
  }

  public boolean didShoot() {
    return didShoot;
  }

  public Map<Player, Square> getInitialPlayerPositions() {
    return new HashMap<>(initialPlayerPositions);
  }

  public void setInitialPlayerPosition(Player player, Square position) {
    initialPlayerPositions.put(player, position);
  }

  public boolean isInitialPositionSet(Player player) {
    return initialPlayerPositions.containsKey(player);
  }

  /**
   * Return currentSelectTargetSlot value.
   *
   * @return currentSelectTargetSlot
   */
  public Integer getCurrentSelectTargetSlot() {
    return currentSelectTargetSlot;
  }

  /**
   * Sets currentSelectTargetSlot value.
   * @param slot
   */
  public void setCurrentSelectTargetSlot(Integer slot) {
    currentSelectTargetSlot = slot;
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

  public boolean isCancelled() {
    return cancelled;
  }

  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  public abstract String getSymbol();

  public abstract boolean isWeapon();
}
