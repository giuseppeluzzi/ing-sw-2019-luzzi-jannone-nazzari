package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.action.game.AfterUsageExecutable;
import it.polimi.se2019.adrenalina.controller.action.game.ExecutableEffect;
import it.polimi.se2019.adrenalina.controller.action.game.GameAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponAction;
import it.polimi.se2019.adrenalina.model.*;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.NotExpose;

import java.util.ArrayList;
import java.util.List;

/**
 * A weapons's effect.
 */
public class Effect implements Buyable {

  private static final long serialVersionUID = 2725086084597119182L;
  private final int costRed;
  private final int costBlue;
  private final int costYellow;
  private final boolean anyTime;
  private boolean indexConfirmed;
  private final String name;
  private final List<WeaponAction> actions;
  private final List<Effect> subEffects;
  @NotExpose
  private Effect requiredEffect;
  @NotExpose
  private Weapon weapon;

  public Effect(String name, Weapon weapon, int costRed, int costBlue, int costYellow,
      boolean anyTime) {
    this.name = name;
    this.weapon = weapon;
    this.costRed = costRed;
    this.costBlue = costBlue;
    this.costYellow = costYellow;
    this.anyTime = anyTime;

    requiredEffect = null;
    actions = new ArrayList<>();
    subEffects = new ArrayList<>();
  }

  public Effect(Effect effect) {
    this(effect, false);
  }

  public Effect(Effect effect, boolean stripActions) {
    name = effect.name;
    weapon = effect.weapon;
    costRed = effect.costRed;
    costBlue = effect.costBlue;
    costYellow = effect.costYellow;
    anyTime = effect.anyTime;
    requiredEffect = effect.requiredEffect;
    if (stripActions) {
      actions = new ArrayList<>();
      subEffects = new ArrayList<>();
      for (Effect subEffect : effect.subEffects) {
        subEffects.add(new Effect(subEffect, true));
      }
    } else {
      actions = effect.getActions();
      subEffects = effect.getSubEffects();
    }
  }


  public String getName() {
    return name;
  }

  /**
   * Returns any effect depending on this one.
   * @return the effect depending on this one if it exists, null otherwise.
   */
  public Effect getRequiredEffect() {
    return requiredEffect;
  }

  public Weapon getWeapon() {
    return weapon;
  }

  public int getCostRed() {
    return costRed;
  }

  public int getCostBlue() {
    return costBlue;
  }

  public int getCostYellow() {
    return costYellow;
  }

  /**
   * Specifies whether the effect can be applied any time during the usage of the weapon.
   * @return true if the effect fan be used any time, false otherwise
   */
  public boolean isAnyTime() {
    return anyTime;
  }

  public void setRequiredEffect(Effect effect) {
    requiredEffect = effect;
  }

  public List<Effect> getSubEffects() {
    return new ArrayList<>(subEffects);
  }

  public Effect getSubEffectByName(String effectName) {
    for (Effect subEffect : subEffects) {
      if (subEffect.name.equalsIgnoreCase(effectName)) {
        return subEffect;
      } else {
        if (!subEffect.getSubEffects().isEmpty()) {
          Effect subEffect2 = subEffect.getSubEffectByName(effectName);
          if (subEffect2 != null) {
            return subEffect2;
          }
        }
      }
    }
    return null;
  }

  public void addSubEffect(Effect effect) {
    subEffects.add(effect);
    effect.setRequiredEffect(this);
  }

  public List<WeaponAction> getActions() {
    return new ArrayList<>(actions);
  }

  public void addAction(WeaponAction action) {
    actions.add(action);
  }

  public boolean isIndexConfirmed() {
    return indexConfirmed;
  }

  /**
   * Determines whether the user has chosen when to apply an anyTimeEffect.
   * @param indexConfirmed true if user has chosen when to apply the anyTimeEffect, false otherwise
   */
  public void setIndexConfirmed(boolean indexConfirmed) {
    this.indexConfirmed = indexConfirmed;
  }

  @Override
  public boolean equals(Object object) {
    return object instanceof Effect &&
        ((Effect) object).name.equals(name) &&
        ((Effect) object).weapon.getName().equals(weapon.getName());
  }

  @Override
  public int hashCode() {
    return costBlue + costRed + costYellow;
  }

  /**
   * Fix references in deserialized effects when subEffects are present.
   * @param ofWeapon the weapon having this effect
   * @param parentEffect the parent effect if this is a subEffect, null otherwise
   */
  public void reconcileDeserialization(Weapon ofWeapon, Effect parentEffect) {
    weapon = ofWeapon;
    requiredEffect = parentEffect;
    if (subEffects != null) {
      for (Effect effect : subEffects) {
        effect.reconcileDeserialization(ofWeapon, this);
      }
    }
  }

  @Override
  public BuyableType getBuyableType() {
    return BuyableType.EFFECT;
  }

  @Override
  public int getCost(AmmoColor ammoColor) {
    switch (ammoColor) {
      case BLUE:
        return costBlue;
      case RED:
        return costRed;
      case YELLOW:
        return costYellow;
      case ANY:
        return 0;
    }
    return 0;
  }

  @Override
  public Buyable getBaseBuyable() {
    return this;
  }

  /**
   * Method called after a payment has been completed.
   * @param turnController the turnController
   * @param board the game board
   * @param player the player
   */
  @Override
  public void afterPaymentCompleted(TurnController turnController, Board board, Player player) {
    Log.debug("Entro nell'after payment completed dell'effetto: " + name);
    Log.debug("La lunghezza dell'actionQueue è: " + turnController.getActionQueueSize());
    Weapon localWeapon = board.getWeaponByName(weapon.getName());
    List<GameAction> turnActions = new ArrayList<>();

    for (WeaponAction action : localWeapon.getEffectByName(name).getActions()) {
      turnActions.add(new ExecutableEffect(turnController, player, localWeapon, action));
    }
    turnActions.add(new AfterUsageExecutable(turnController, player, localWeapon));

    turnController.addTurnActions(turnActions);
    Log.debug("Dopo l'aggiunta delle azioni la lunghezza della coda è: " + turnController.getActionQueueSize());
    turnController.executeGameActionQueue();
  }

  @Override
  public String promptMessage() {
    return name;
  }
}
