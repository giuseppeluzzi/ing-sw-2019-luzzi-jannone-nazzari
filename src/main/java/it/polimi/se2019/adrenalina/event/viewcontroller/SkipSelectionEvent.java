package it.polimi.se2019.adrenalina.event.viewcontroller;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Event fired when a player skips the selection of a target during an attack.
 */
public class SkipSelectionEvent implements Event {

  private static final long serialVersionUID = -2874939330685152878L;

  @Override
  public EventType getEventType() {
    return EventType.SKIP_SELECTION_EVENT;
  }
}
