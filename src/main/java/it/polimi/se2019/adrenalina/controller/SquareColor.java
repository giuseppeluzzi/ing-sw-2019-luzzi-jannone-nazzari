package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.utils.ANSIColor;

public enum SquareColor {
  GREEN(ANSIColor.GREEN),
  BLUE(ANSIColor.BLUE),
  PURPLE(ANSIColor.MAGENTA),
  GREY(ANSIColor.WHITE),
  YELLOW(ANSIColor.YELLOW),
  RED(ANSIColor.RED);

  private final ANSIColor ansiColor;

  SquareColor(ANSIColor ansiColor) {
    this.ansiColor = ansiColor;
  }

  public ANSIColor getAnsiColor() {
    return ansiColor;
  }
}
