package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Event sent when the maximum number of killshots for the game is set.
 */
public class BoardSkullsUpdate implements Event {

  private static final long serialVersionUID = -1459684764328696748L;
  private final int skulls;

  public BoardSkullsUpdate(int skulls) {
    this.skulls = skulls;
  }

  public int getSkulls() {
    return skulls;
  }

  @Override
  public EventType getEventType() {
    return EventType.BOARD_SKULLS_UPDATE;
  }
}
