package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.utils.ANSIColor;

/**
 * Enumeration of player colors with their names.
 */
public enum PlayerColor {
  GREEN(ANSIColor.GREEN, "SPROG", "#4CAF50"),
  BLUE(ANSIColor.BLUE, "BANSHEE", "#3F51B5"),
  PURPLE(ANSIColor.MAGENTA, "VIOLETTA", "#9C27B0"),
  GREY(ANSIColor.WHITE, "DOZER", "#212121"),
  YELLOW(ANSIColor.YELLOW, ":D-STRUTT-0R3", "#FF9800");

  private final ANSIColor ansiColor;
  private final String characterName;
  private final String hexColor;

  PlayerColor(ANSIColor ansiColor, String characterName, String hexColor) {
    this.ansiColor = ansiColor;
    this.characterName = characterName;
    this.hexColor = hexColor;
  }

  public ANSIColor getAnsiColor() {
    return ansiColor;
  }

  public String getCharacterName() {
    return characterName;
  }

  public String getHexColor() {
    return hexColor;
  }
}
