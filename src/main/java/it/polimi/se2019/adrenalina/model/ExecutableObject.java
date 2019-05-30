package it.polimi.se2019.adrenalina.model;


import java.util.Map;

public interface ExecutableObject {
  /**
   * Clear data of previous targets.
   */
  void reset();

  /**
   * Return Target element from targetHistory at index "key".
   * @param key index of the target requested
   * @return Target element at specified index
   */
  Target getTargetHistory(Integer key);

  /**
   * Insert in targetHistory at index "key" Target "value".
   * @param key index where Target has to be inserted
   * @param value Target to be inserted
   */
  void setTargetHistory(Integer key, Target value);

  /**
   * Returns Player owning the object, throws an exception otherwise.
   * @return Player if existing
   */
  Player getOwner();

  void setDidShoot();

  boolean didShoot();

  Map<Player, Square> getInitialPlayerPositions();

  void setInitialPlayerPosition(Player player, Square position);

  Integer getCurrentSelectTargetSlot();

  void setCurrentSelectTargetSlot(Integer slot);

  boolean isInitialPositionSet(Player player);
}
