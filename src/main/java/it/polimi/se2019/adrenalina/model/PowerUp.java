package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.controller.action.weapon.OptionalMoveAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponAction;
import it.polimi.se2019.adrenalina.exceptions.InvalidPowerUpException;
import it.polimi.se2019.adrenalina.utils.NotExpose;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PowerUp implements Spendable, ExecutableObject, Buyable {

  private static final long serialVersionUID = 8948751912601215729L;
  private final AmmoColor color;
  private final List<WeaponAction> actions;
  private final boolean doesCost;
  private final PowerUpType type;
  private int currentSelectTargetSlot;
  @NotExpose
  private HashMap<Integer, Target> targetHistory = new HashMap<>();

  protected PowerUp(AmmoColor color, boolean doesCost, PowerUpType type) {
    this.type = type;
    this.color = color;
    this.doesCost = doesCost;
    actions = new ArrayList<>();
  }

  public PowerUpType getType() {
    return type;
  }

  public abstract boolean canUse();

  public abstract PowerUp copy();

  public List<WeaponAction> getActions() {
    return new ArrayList<>(actions);
  }

  public void addAction(WeaponAction action) {
    actions.add(action);
  }

  public abstract String getName();

  public boolean doesCost() {
    return doesCost;
  }

  @Override
  public AmmoColor getColor() {
    return color;
  }

  @Override
  public String getSpendableName() {
    return getName() + " " + color;
  }

  @Override
  public boolean isPowerUp() {
    return true;
  }

  public void addOptionalMoveAction(OptionalMoveAction action) {
    // this type of weaponaction needs to be executed at the begin and at the end
    actions.add(0, action);
    actions.add(action);
  }

  public String serialize() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  @Override
  public void reset() {
    targetHistory.clear();
  }

  @Override
  public Target getTargetHistory(Integer key) {
    return targetHistory.get(key);
  }

  @Override
  public void setTargetHistory(Integer key, Target value) {
    targetHistory.put(key, value);
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

  @Override
  public BuyableType getBuyableType() {
    return BuyableType.POWERUP;
  }

  @Override
  public int getCost(AmmoColor ammoColor) {
    return 0;
  }

  @Override
  public void afterPaymentCompleted(TurnController turnController, Board board, Player player) {
    PowerUp powerUp = player.getPowerUp(type, color);
    if (powerUp != null) {
      board.undrawPowerUp(powerUp);
      try {
        player.removePowerUp(powerUp);
      } catch (InvalidPowerUpException ignored) {
        // ignore exception
      }
    }
  }

  @Override
  public void setDidShoot() {
    // Not implemented in powerUps
  }

  @Override
  public boolean didShoot() {
    // Not implemented in powerUps
    return true;
  }

  @Override
  public Map<Player, Square> getInitialPlayerPositions() {
    // Not implemented in powerUps
    return null;
  }

  @Override
  public void setInitialPlayerPosition(Player player, Square position) {
    // Not implemented in powerUps
  }

  @Override
  public Integer getCurrentSelectTargetSlot() {
    return currentSelectTargetSlot;
  }

  @Override
  public void setCurrentSelectTargetSlot(Integer slot) {
    currentSelectTargetSlot = slot;
  }

  @Override
  public boolean isInitialPositionSet(Player player) {
    // Not implemented in powerUps
    return true;
  }
}