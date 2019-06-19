package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.model.Spendable;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
import java.util.ArrayList;
import java.util.List;

/**
 * Enumeration of all possible colors of an ammo.
 */
public enum AmmoColor implements Spendable {
  BLUE(SquareColor.BLUE, ANSIColor.BLUE),
  RED(SquareColor.RED, ANSIColor.RED),
  YELLOW(SquareColor.YELLOW, ANSIColor.YELLOW),
  ANY(ANSIColor.WHITE);

  private final SquareColor equivalentSquareColor;
  private final ANSIColor ansiColor;

  AmmoColor(ANSIColor ansiColor) {
    equivalentSquareColor = null;
    this.ansiColor = ansiColor;
  }

  AmmoColor(SquareColor equivalentSquareColor, ANSIColor ansiColor) {
    this.equivalentSquareColor = equivalentSquareColor;
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

  public SquareColor getEquivalentSquareColor() {
    return equivalentSquareColor;
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
