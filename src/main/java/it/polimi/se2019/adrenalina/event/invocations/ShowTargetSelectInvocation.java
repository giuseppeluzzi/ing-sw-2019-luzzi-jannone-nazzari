package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * Invocation to show target selection for shooting on the client.
 */
public class ShowTargetSelectInvocation implements Invocation {

  private static final long serialVersionUID = 8037594543903190668L;
  private final TargetType targetType;
  private final List<Target> targets;
  private final boolean skippable;

  public ShowTargetSelectInvocation(
      TargetType targetType, List<Target> targets, boolean skippable) {
    this.targetType = targetType;
    this.targets = new ArrayList<>(targets);
    this.skippable = skippable;
  }

  public TargetType getTargetType() {
    return targetType;
  }

  public List<Target> getTargets() {
    return new ArrayList<>(targets);
  }

  public boolean isSkippable() {
    return skippable;
  }

  @Override
  public EventType getEventType() {
    return EventType.SHOW_TARGET_SELECT_INVOCATION;
  }
}
