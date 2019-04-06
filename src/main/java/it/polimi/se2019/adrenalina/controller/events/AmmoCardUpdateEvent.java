package it.polimi.se2019.adrenalina.controller.events;

import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Square;

public class AmmoCardUpdateEvent implements Event {
  private final Square square;
  private final AmmoCard ammoCard;

  public AmmoCardUpdateEvent(Square square, AmmoCard ammoCard) {
    this.square = square;
    this.ammoCard = ammoCard;
  }

  public Square getSquare() {
    return square;
  }

  public AmmoCard getAmmoCard() {
    return ammoCard;
  }
}
