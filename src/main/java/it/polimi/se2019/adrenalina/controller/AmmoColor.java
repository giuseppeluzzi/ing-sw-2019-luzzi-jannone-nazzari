package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.model.Spendable;
import java.util.ArrayList;
import java.util.List;

/**
 * Enumeration of all possible colors of an ammo
 */
public enum AmmoColor implements Spendable {
  BLUE,
  RED,
  YELLOW,
  ANY;

  public static List<AmmoColor> getValidColor() {
    List<AmmoColor> list = new ArrayList<>();
    list.add(BLUE);
    list.add(RED);
    list.add(YELLOW);
    return list;
  }

  @Override
  public AmmoColor getSpendableColor() {
    return this;
  }

  @Override
  public String getSpendableName() {
    return "Munizione " + this;
  }

  @Override
  public boolean isPowerUp() {
    return false;
  }
}
