package it.polimi.se2019.adrenalina.event;

import it.polimi.se2019.adrenalina.model.AmmoCard;

public class AmmoCardUpdateEvent implements Event {

  private static final long serialVersionUID = 6394659975918667079L;

  private final int squareX;
  private final int squareY;
  private final AmmoCard ammoCard;

  public AmmoCardUpdateEvent(int squareX, int squareY,
      AmmoCard ammoCard) {
    this.squareX = squareX;
    this.squareY = squareY;
    this.ammoCard = ammoCard;
  }

  public int getSquareX() {
    return squareX;
  }

  public int getSquareY() {
    return squareY;
  }

  public AmmoCard getAmmoCard() {
    return ammoCard;
  }

  @Override
  public EventType getEventType() {
    return EventType.AMMO_CARD_UPDATE_EVENT;
  }
}
