package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.event.EventType;

public class ShowFinalRanksInvocation implements Invocation {

  private static final long serialVersionUID = 8534784928452695748L;

  @Override
  public EventType getEventType() {
    return EventType.SHOW_FINAL_RANKS_INVOCATION;
  }
}
