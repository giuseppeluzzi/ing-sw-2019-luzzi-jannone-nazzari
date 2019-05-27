package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

public class BoardHasAmmoCardsUpdate implements Event {

  private static final long serialVersionUID = 3101088508036419453L;
  private final boolean empty;

  public BoardHasAmmoCardsUpdate(boolean empty) {
    this.empty = empty;
  }

  public boolean isEmpty() {
    return empty;
  }

  @Override
  public EventType getEventType() {
    return EventType.BOARD_HAS_AMMO_CARDS_UPDATE;
  }
}
