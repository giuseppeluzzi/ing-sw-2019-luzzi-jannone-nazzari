package it.polimi.se2019.adrenalina.controller;

public enum SquareColor {
  GREEN("\u001b[32m"),
  BLUE("\u001b[34m"),
  PURPLE("\u001b[35m"),
  GREY("\u001b[37m"),
  YELLOW("\u001b[33m"),
  RED("\u001b[31m");

  private final String ansiColor;

  SquareColor(String ansiColor) {
    this.ansiColor = ansiColor;
  }

  public String getAnsiColor() {
    return ansiColor;
  }
}
