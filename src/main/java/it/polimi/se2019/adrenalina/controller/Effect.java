package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.model.Weapon;
import java.util.ArrayList;
import java.util.List;

public class Effect {
  private final int costRed;
  private final int costBlue;
  private final int costYellow;
  private final String name;
  private Effect requiredEffect;
  private final Weapon weapon;
  private final List<Action> actions;
  private final List<Effect> subEffects;

  public Effect(String name, Weapon weapon, int costRed, int costBlue, int costYellow) {
    this.name = name;
    this.weapon = weapon;
    this.costRed = costRed;
    this.costBlue = costBlue;
    this.costYellow = costYellow;

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
    requiredEffect = effect.getRequiredEffect();
    actions = effect.getActions();
    subEffects = effect.getSubEffects();
  }

  public String getName() {
    return name;
  }

  public Effect getRequiredEffect() {
    if (requiredEffect == null) {
      return null;
    } else {
      return new Effect(requiredEffect);
    }
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

  public void setRequiredEffect(Effect effect) {
    requiredEffect = effect;
  }

  public List<Effect> getSubEffects() {
    List<Effect> output = new ArrayList<>();
    for (Effect effect : subEffects) {
      output.add(new Effect(effect));
    }
    return output;
  }

  public void addSubEffect(Effect effect) {
    subEffects.add(effect);
  }

  public List<Action> getActions() {
    // TODO: actions is mutable
    return new ArrayList<>();
  }

  public void addAction(Action action) {
    actions.add(action);
  }

  public void addOptionalMoveAction(OptionalMoveAction action) {
    // this type of action needs to be executed at the begin and at the end
    actions.add(0, action);
    actions.add(action);
  }
}
