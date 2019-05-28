package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

public class BoardHasWeaponsUpdate implements Event {

  private static final long serialVersionUID = -1459684764328696748L;
  private final boolean weapons;

  public BoardHasWeaponsUpdate(boolean weapons) {
    this.weapons = weapons;
  }

  public boolean hasWeapons() {
    return weapons;
  }

  @Override
  public EventType getEventType() {
    return EventType.BOARD_HAS_WEAPON_UPDATE;
  }
}
