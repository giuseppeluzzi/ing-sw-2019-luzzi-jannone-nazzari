package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.utils.ANSIColor;

/**
 * Enumeration of square colors.
 */
public enum SquareColor {
  GREEN(ANSIColor.GREEN),
  BLUE(ANSIColor.BLUE, AmmoColor.BLUE),
  PURPLE(ANSIColor.MAGENTA),
  GREY(ANSIColor.WHITE),
  YELLOW(ANSIColor.YELLOW, AmmoColor.YELLOW),
  RED(ANSIColor.RED, AmmoColor.RED);

  private final ANSIColor ansiColor;
  private final AmmoColor equivalentAmmoColor;

  SquareColor(ANSIColor ansiColor) {
    this.ansiColor = ansiColor;
    equivalentAmmoColor = null;
  }

  SquareColor(ANSIColor ansiColor, AmmoColor equivalentAmmoColor) {
    this.ansiColor = ansiColor;
    this.equivalentAmmoColor = equivalentAmmoColor;
  }

  public ANSIColor getAnsiColor() {
    return ansiColor;
  }

  public AmmoColor getEquivalentAmmoColor() {
    return equivalentAmmoColor;
  }
}
