package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.action.Action;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.NotExpose;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Effect implements Serializable {
  private final int costRed;
  private final int costBlue;
  private final int costYellow;
  private final boolean anyTime;
  private final String name;
  @NotExpose private Effect requiredEffect;
  @NotExpose private Weapon weapon;
  private final List<Action> actions;
  private final List<Effect> subEffects;

  public Effect(String name, Weapon weapon, int costRed, int costBlue, int costYellow, boolean anyTime) {
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
    name = effect.name;
    weapon = effect.weapon;
    costRed = effect.costRed;
    costBlue = effect.costBlue;
    costYellow = effect.costYellow;
    anyTime = effect.anyTime;
    requiredEffect = effect.getRequiredEffect();
    actions = effect.getActions();
    subEffects = effect.getSubEffects();
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

  public List<Action> getActions() {
    return new ArrayList<>(actions);
  }

  public void addAction(Action action) {
    actions.add(action);
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
}
