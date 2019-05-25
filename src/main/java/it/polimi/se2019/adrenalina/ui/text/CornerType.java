package it.polimi.se2019.adrenalina.ui.text;

public enum CornerType {
  TOP_LEFT_CORNER("┏"),
  TOP_RIGHT_CORNER("┓"),
  BOTTOM_LEFT_CORNER("┗"),
  BOTTOM_RIGHT_CORNER("┛");

  private final String string;

  CornerType(String string) {
    this.string = string;
  }

  public String toString() {
    return string;
  }
}
