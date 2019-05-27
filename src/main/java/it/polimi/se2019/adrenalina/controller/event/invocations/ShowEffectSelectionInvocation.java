package it.polimi.se2019.adrenalina.controller.event.invocations;

import it.polimi.se2019.adrenalina.controller.event.EventType;
import it.polimi.se2019.adrenalina.model.Weapon;

public class ShowEffectSelectionInvocation implements Invocation {

  private static final long serialVersionUID = 2421777681566272744L;
  private final Weapon weapon;

  public ShowEffectSelectionInvocation(Weapon weapon) {
    this.weapon = weapon;
  }

  public Weapon getWeapon() {
    return weapon;
  }

  @Override
  public EventType getEventType() {
    return EventType.SHOW_EFFECT_SELECTION_INVOCATION;
  }
}
