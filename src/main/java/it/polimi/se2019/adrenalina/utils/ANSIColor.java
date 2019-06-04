package it.polimi.se2019.adrenalina.utils;

public enum ANSIColor {
  BLACK("\u001b[30m", "\u001b[30m"),
  RED("\u001b[91m", "\u001b[2;91m"),
  GREEN("\u001b[92m", "\u001b[2;92m"),
  YELLOW("\u001b[93m", "\u001b[2;93m"),
  BLUE("\u001b[96m", "\u001b[2;96m"), // using 96 (cyan) instead of 94 for visibility
  MAGENTA("\u001b[95m", "\u001b[2;95m"),
  CYAN("\u001b[96m", "\u001b[2;96m"),
  WHITE("\u001b[37m", "\u001b[2;97m"),
  RESET("\u001b[0m", "\u001b[0m");

  private final String normalColor;
  private final String dimColor;

  ANSIColor(String ansiColor, String dimColor) {
    normalColor = ansiColor;
    this.dimColor = dimColor;
  }

  @Override
  public String toString() {
    return normalColor;
  }

  public String toString(boolean dim) {
    if (dim) {
      return dimColor;
    } else {
      return normalColor;
    }
  }
}
