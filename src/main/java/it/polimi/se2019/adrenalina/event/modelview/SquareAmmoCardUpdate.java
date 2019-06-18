package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Event sent when the ammo card on a square is updated.
 */
public class SquareAmmoCardUpdate implements Event {

  private static final long serialVersionUID = 6103073463511121388L;
  private final int blue;
  private final int red;
  private final int yellow;
  private final int powerUps;
  private final int posX;
  private final int posY;

  public SquareAmmoCardUpdate(int posX, int posY, int blue, int red, int yellow, int powerUps) {
    this.blue = blue;
    this.red = red;
    this.yellow = yellow;
    this.powerUps = powerUps;
    this.posX = posX;
    this.posY = posY;
  }

  public int getBlue() {
    return blue;
  }

  public int getRed() {
    return red;
  }

  public int getYellow() {
    return yellow;
  }

  public int getPowerUps() {
    return powerUps;
  }

  public int getPosX() {
    return posX;
  }

  public int getPosY() {
    return posY;
  }

  @Override
  public EventType getEventType() {
    return EventType.SQUARE_AMMO_CARD_UPDATE;
  }
}
