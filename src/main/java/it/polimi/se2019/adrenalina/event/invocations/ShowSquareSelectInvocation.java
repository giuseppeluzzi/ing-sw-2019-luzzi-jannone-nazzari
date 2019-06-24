package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Invocation to show the square selection for shooting on the client.
 */
public class ShowSquareSelectInvocation implements Invocation {

  private static final long serialVersionUID = 1306406873782692471L;
  private final List<Square> targets;

  public ShowSquareSelectInvocation(List<Square> targets) {
    this.targets = new ArrayList<>(targets);
  }

  public List<Square> getTargets() {
    return new ArrayList<>(targets);
  }

  @Override
  public EventType getEventType() {
    return EventType.SHOW_SQUARE_SELECT_INVOCATION;
  }
}
