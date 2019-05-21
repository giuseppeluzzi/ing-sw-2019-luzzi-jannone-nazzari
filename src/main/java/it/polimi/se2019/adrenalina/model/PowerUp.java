package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponAction;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.action.weapon.OptionalMoveAction;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class PowerUp implements Serializable, Spendable {

  private static final long serialVersionUID = 8948751912601215729L;
  private final AmmoColor color;
  private final List<WeaponAction> actions;

  protected PowerUp(AmmoColor color) {
    this.color = color;
    actions = new ArrayList<>();
  }

  public abstract boolean canUse();

  public abstract PowerUp copy();

  public AmmoColor getColor() {
    return color;
  }

  public List<WeaponAction> getActions() {
    return new ArrayList<>(actions);
  }

  public void addAction(WeaponAction action) {
    actions.add(action);
  }

  public abstract String getName();

  @Override
  public AmmoColor getSpendableColor() {
    return color;
  }

  @Override
  public String getSpendableName() {
    return getName();
  }

  public void addOptionalMoveAction(OptionalMoveAction action) {
    // this type of weaponaction needs to be executed at the begin and at the end
    actions.add(0, action);
    actions.add(action);
  }

  public String serialize(){
    Gson gson = new Gson();
    return gson.toJson(this);
  }
}