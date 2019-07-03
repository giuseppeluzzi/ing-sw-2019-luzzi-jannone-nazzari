package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.utils.ANSIColor;

/**
 * Enumeration of player colors with their names.
 */
public enum PlayerColor {
  GREEN(ANSIColor.GREEN, "SPROG"),
  BLUE(ANSIColor.BLUE, "BANSHEE"),
  PURPLE(ANSIColor.MAGENTA, "VIOLETTA"),
  GREY(ANSIColor.WHITE, "DOZER"),
  YELLOW(ANSIColor.YELLOW, ":D-STRUTT-0R3");

  private final ANSIColor ansiColor;
  private final String characterName;

  PlayerColor(ANSIColor ansiColor, String characterName) {
    this.ansiColor = ansiColor;
    this.characterName = characterName;
  }

  public ANSIColor getAnsiColor() {
    return ansiColor;
  }

  public String getCharacterName() {
    return characterName;
  }

  public String getHexColor() {
    return ansiColor.getHexColor();
  }
}
