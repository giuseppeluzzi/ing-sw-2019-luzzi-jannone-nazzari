package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Target;
import java.util.ArrayList;
import java.util.List;

public class ShowSquareSelectInvocation implements Invocation {

  private static final long serialVersionUID = 1306406873782692471L;
  private final List<Target> targets;

  public ShowSquareSelectInvocation(List<Target> targets) {
    this.targets = new ArrayList<>(targets);
  }

  public List<Target> getTargets() {
    return new ArrayList<>(targets);
  }

  @Override
  public EventType getEventType() {
    return EventType.SHOW_SQUARE_SELECT_INVOCATION;
  }
}
