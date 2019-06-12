package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.action.game.GameAction;
import it.polimi.se2019.adrenalina.controller.action.game.ExecutableEffect;
import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponAction;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Buyable;
import it.polimi.se2019.adrenalina.model.BuyableType;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.NotExpose;
import java.util.ArrayList;
import java.util.List;

public class Effect implements Buyable {

  private static final long serialVersionUID = 2725086084597119182L;
  private final int costRed;
  private final int costBlue;
  private final int costYellow;
  private final boolean anyTime;
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

  public boolean isAnyTime() {
    return anyTime;
  }

  public void setRequiredEffect(Effect effect) {
    requiredEffect = effect;
  }

  public List<Effect> getSubEffects() {
    return new ArrayList<>(subEffects);
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

  public void reconcileDeserialization(Weapon ofWeapon, Effect parentEffect) {
    weapon = ofWeapon;
    requiredEffect = parentEffect;
    if (parentEffect != null) {
      for (Effect effect : parentEffect.subEffects) {
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
  public void afterPaymentCompleted(TurnController turnController, Board board, Player player) {
    Weapon localWeapon = board.getWeaponByName(weapon.getName());
    List<GameAction> turnActions = new ArrayList<>();

    for (WeaponAction action : localWeapon.getEffectByName(name).getActions()) {
      turnActions.add(new ExecutableEffect(turnController, player, localWeapon, action, true));
    }

    turnController.addTurnActions(turnActions);
    turnController.executeGameActionQueue();
  }
}
