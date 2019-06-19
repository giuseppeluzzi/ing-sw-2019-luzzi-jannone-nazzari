package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Event indicating whether the board has ammo cards left in the stack.
 */
public class BoardHasAmmoCardsUpdate implements Event {

  private static final long serialVersionUID = 3101088508036419453L;
  private final boolean ammoCards;

  public BoardHasAmmoCardsUpdate(boolean ammoCards) {
    this.ammoCards = ammoCards;
  }

  public boolean hasAmmoCards() {
    return ammoCards;
  }

  @Override
  public EventType getEventType() {
    return EventType.BOARD_HAS_AMMO_CARDS_UPDATE;
  }
}
