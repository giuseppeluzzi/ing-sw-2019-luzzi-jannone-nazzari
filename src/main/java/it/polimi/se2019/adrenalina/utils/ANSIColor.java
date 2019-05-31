package it.polimi.se2019.adrenalina.utils;

public enum ANSIColor {
  BLACK("\u001b[30m"),
  RED("\u001b[91m"),
  GREEN("\u001b[92m"),
  YELLOW("\u001b[93m"),
  BLUE("\u001b[94m"),
  MAGENTA("\u001b[95m"),
  CYAN("\u001b[96m"),
  WHITE("\u001b[97m"),
  DIM_RED("\u001b[31m"),
  DIM_GREEN("\u001b[32m"),
  DIM_YELLOW("\u001b[33m"),
  DIM_BLUE("\u001b[34m"),
  DIM_MAGENTA("\u001b[35m"),
  DIM_CYAN("\u001b[36m"),
  DIM_WHITE("\u001b[37m"),
  RESET("\u001b[0m");

  private final String ansiColor;

  ANSIColor(String ansiColor) {
    this.ansiColor = ansiColor;
  }

  @Override
  public String toString() {
    return ansiColor;
  }
}
