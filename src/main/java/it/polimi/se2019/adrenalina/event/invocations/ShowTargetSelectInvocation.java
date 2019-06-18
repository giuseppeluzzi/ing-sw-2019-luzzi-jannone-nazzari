package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Target;
import java.util.ArrayList;
import java.util.List;

/**
 * Invocation that has the client show target selection for shooting.
 * @see Invocation
 */
public class ShowTargetSelectInvocation implements Invocation {

  private static final long serialVersionUID = 8037594543903190668L;
  private final TargetType targetType;
  private final List<Target> targets;

  public ShowTargetSelectInvocation(
      TargetType targetType, List<Target> targets) {
    this.targetType = targetType;
    this.targets = new ArrayList<>(targets);
  }

  public TargetType getTargetType() {
    return targetType;
  }

  public List<Target> getTargets() {
    return new ArrayList<>(targets);
  }

  @Override
  public EventType getEventType() {
    return EventType.SHOW_TARGET_SELECT_INVOCATION;
  }
}
