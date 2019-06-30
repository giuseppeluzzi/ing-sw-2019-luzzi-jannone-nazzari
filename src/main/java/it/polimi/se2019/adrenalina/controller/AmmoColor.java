package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.model.Spendable;
import it.polimi.se2019.adrenalina.utils.ANSIColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumeration of all possible colors of an ammo.
 */
public enum AmmoColor implements Spendable {
  BLUE(ANSIColor.BLUE),
  RED(ANSIColor.RED),
  YELLOW(ANSIColor.YELLOW),
  ANY(ANSIColor.WHITE);

  private final ANSIColor ansiColor;

  AmmoColor(ANSIColor ansiColor) {
    this.ansiColor = ansiColor;
  }

  public static List<AmmoColor> getValidColor() {
    List<AmmoColor> list = new ArrayList<>();
    list.add(BLUE);
    list.add(RED);
    list.add(YELLOW);
    return list;
  }

  public ANSIColor getAnsiColor() {
    return ansiColor;
  }
  
  @Override
  public AmmoColor getColor() {
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
