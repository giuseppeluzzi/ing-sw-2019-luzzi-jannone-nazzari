package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

public class BoardHasWeaponsUpdate implements Event {

  private static final long serialVersionUID = -1459684764328696748L;
  private final boolean empty;

  public BoardHasWeaponsUpdate(boolean empty) {
    this.empty = empty;
  }

  public boolean isEmpty() {
    return empty;
  }

  @Override
  public EventType getEventType() {
    return EventType.BOARD_HAS_WEAPON_UPDATE;
  }
}
