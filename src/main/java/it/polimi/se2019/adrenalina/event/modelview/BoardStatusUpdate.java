package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

public class BoardStatusUpdate implements Event {

  private static final long serialVersionUID = -7116650564967462703L;
  private final String status;

  public BoardStatusUpdate(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  @Override
  public EventType getEventType() {
    return EventType.BOARD_STATUS_UPDATE;
  }
}
