package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Invocation to print the unsuspend prompt for suspended players on the client.
 */
public class ShowUnsuspendPromptInvocation implements Invocation {

  private static final long serialVersionUID = 1523660012369856301L;

  @Override
  public EventType getEventType() {
    return EventType.SHOW_UNSUSPEND_PROMPT_INVOCATION;
  }
}
