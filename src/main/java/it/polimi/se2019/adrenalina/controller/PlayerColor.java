package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.utils.ANSIColor;

public enum PlayerColor {
  GREEN(ANSIColor.GREEN),
  BLUE(ANSIColor.BLUE),
  PURPLE(ANSIColor.MAGENTA),
  GREY(ANSIColor.WHITE),
  YELLOW(ANSIColor.YELLOW);

  private final ANSIColor ansiColor;

  PlayerColor(ANSIColor ansiColor) {
    this.ansiColor = ansiColor;
  }

  public ANSIColor getAnsiColor() {
    return ansiColor;
  }
}
