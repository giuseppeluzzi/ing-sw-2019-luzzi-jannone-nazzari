package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Invocation to show the direction selection for a movement on the client.
 */
public class ShowDirectionSelectInvocation implements Invocation {

  private static final long serialVersionUID = 351480962233622370L;

  @Override
  public EventType getEventType() {
    return EventType.SHOW_DIRECTION_SELECT_INVOCATION;
  }
}
