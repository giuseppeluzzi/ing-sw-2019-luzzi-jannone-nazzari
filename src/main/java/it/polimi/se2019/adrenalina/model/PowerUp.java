package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponAction;
import java.util.ArrayList;
import java.util.List;

/**
 * Power up object.
 */
public abstract class PowerUp extends ExecutableObject implements Spendable, Buyable {

  private static final long serialVersionUID = 8948751912601215729L;
  private final AmmoColor color;
  private final transient List<WeaponAction> actions;
  private final boolean doesCost;
  private final PowerUpType type;

  protected PowerUp(AmmoColor color, boolean doesCost, PowerUpType type) {
    this.type = type;
    this.color = color;
    this.doesCost = doesCost;
    actions = new ArrayList<>();
  }

  public PowerUpType getType() {
    return type;
  }

  public abstract PowerUp copy();

  public final List<WeaponAction> getActions() {
    return new ArrayList<>(actions);
  }

  public final void addAction(WeaponAction action) {
    actions.add(action);
  }

  public abstract String getName();

  /**
   * Specifies whether the power up has a cost or not.
   *
   * @return true if the power up has a cost, false if it's free
   */
  public boolean doesCost() {
    return doesCost;
  }

  @Override
  public AmmoColor getColor() {
    return color;
  }

  /**
   * @return human readable string with the name and color of this power up (for use as a spendable)
   */
  @Override
  public String getSpendableName() {
    return getName() + " " + color;
  }

  @Override
  public boolean isPowerUp() {
    return true;
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
  public Buyable getBaseBuyable() {
    return this;
  }

  @Override
  public void afterPaymentCompleted(TurnController turnController, Board board, Player player) {
    throw new IllegalStateException("Must use a decorator");
  }

  @Override
  public boolean isWeapon() {
    return false;
  }

  @Override
  public int hashCode() {
    return type.hashCode() + 2 * color.hashCode();
  }

  @Override
  public boolean equals(Object object) {
    return object instanceof PowerUp &&
        ((PowerUp) object).getType() == type && ((PowerUp) object).getColor() == color;
  }
}