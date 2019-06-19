package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Invocation to print the game board on the client.
 */
public class ShowBoardInvocation implements Invocation {

  private static final long serialVersionUID = -2120937465673929567L;

  @Override
  public EventType getEventType() {
    return EventType.SHOW_BOARD_INVOCATION;
  }
}
