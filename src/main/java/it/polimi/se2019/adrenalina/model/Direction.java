package it.polimi.se2019.adrenalina.model;

/**
 * Enumeration of the four cardinal directions.
 */
public enum Direction {
  NORTH("Nord"),
  EAST("Est"),
  SOUTH("Sud"),
  WEST("Ovest");

  private final String name;

  Direction(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
