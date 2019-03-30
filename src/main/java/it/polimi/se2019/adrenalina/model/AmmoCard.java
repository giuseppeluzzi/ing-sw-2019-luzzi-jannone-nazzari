package it.polimi.se2019.adrenalina.model;

public class AmmoCard {
  private final int red;
  private final int blue;
  private final int yellow;
  private final int powerUp;

  public AmmoCard(int red, int blue, int yellow, int powerUp) {
    this.red = red;
    this.blue = blue;
    this.yellow = yellow;
    this.powerUp = powerUp;
  }

  public int getRed() {
    return red;
  }

  public int getBlue() {
    return blue;
  }

  public int getYellow() {
    return yellow;
  }

  public int getPowerUp() {
    return powerUp;
  }
}
