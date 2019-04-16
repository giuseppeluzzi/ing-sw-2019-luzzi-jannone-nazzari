package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.Action;
import it.polimi.se2019.adrenalina.controller.OptionalMoveAction;
import java.util.ArrayList;
import java.util.List;

public abstract class PowerUp {
  private final AmmoColor color;
  private final List<Action> actions;

  protected PowerUp(AmmoColor color) {
    this.color = color;
    actions = new ArrayList<>();
  }

  public abstract boolean canUse();

  public abstract PowerUp copy();

  public AmmoColor getColor() {
    return color;
  }

  public List<Action> getActions() {
    return new ArrayList<>(actions);
  }

  public void addAction(Action action) {
    actions.add(action);
  }

  public void addOptionalMoveAction(OptionalMoveAction action) {
    // this type of action needs to be executed at the begin and at the end
    actions.add(0, action);
    actions.add(action);
  }

  public String serialize(){
    Gson gson = new Gson();
    return gson.toJson(this);
  }
}