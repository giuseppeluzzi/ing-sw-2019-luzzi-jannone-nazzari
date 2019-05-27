package it.polimi.se2019.adrenalina.controller.event.invocations;

import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.controller.event.EventType;
import java.util.ArrayList;
import java.util.List;

public class ShowTurnActionSelectionInvocation implements Invocation {

  private static final long serialVersionUID = -8445507219354141631L;
  private final List<TurnAction> actions;

  public ShowTurnActionSelectionInvocation(
      List<TurnAction> actions) {
    this.actions = new ArrayList<>(actions);
  }

  public List<TurnAction> getActions() {
    return new ArrayList<>(actions);
  }

  @Override
  public EventType getEventType() {
    return EventType.SHOW_TURN_ACTION_SELECTION_INVOCATION;
  }
}
